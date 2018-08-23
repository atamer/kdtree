package com.jumbo.store.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumbo.store.constants.Constants;
import com.jumbo.store.dto.StoreItemDTO;
import com.jumbo.store.exception.GeoLocationException;
import com.jumbo.store.exception.InvalidKDTreeException;
import com.jumbo.store.json.StoreHolder;
import com.jumbo.store.json.StoreItem;
import com.jumbo.store.kdtree.Box;
import com.jumbo.store.kdtree.DistanceStoreItem;
import com.jumbo.store.kdtree.KDNode;
import com.jumbo.store.kdtree.Point;
import com.jumbo.store.validator.StoreValidator;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class KDServiceImpl implements KDService {


    private static final Logger logger = Logger.getLogger(KDServiceImpl.class);
    private boolean isValidTree;
    private KDNode root;
    private Integer totalStoreNumber;
    private Map<String, StoreItem> storeItemMap;


    public KDServiceImpl(@Value("${json_file_name}") String jsonLocation, ObjectMapper jacksonObjectMapper, StoreValidator storeValidator) {
        if (jsonLocation == null) {
            jsonLocation = "/json/stores.json";
        }
        InputStream is = getClass().getResourceAsStream(jsonLocation);
        try {
            StoreHolder storeHolder = jacksonObjectMapper.readerFor(new TypeReference<StoreHolder>() {
            }).readValue(is);
            List<StoreItem> storeItemList = Collections.unmodifiableList(storeHolder.getStores().stream().filter(storeValidator::isValid).collect(Collectors.toList()));
            totalStoreNumber = storeItemList.size();
            root = buildKDTree(storeItemList, 0);
            storeItemMap = new HashMap<>();
            storeItemList.forEach((StoreItem storeItem) -> {
                if (storeItem.getUuid().isPresent()) {
                    storeItemMap.put(storeItem.getUuid().get(), storeItem);
                }
            });
            storeItemMap = Collections.unmodifiableMap(storeItemMap);
            isValidTree = true;
        } catch (Exception e) {
            isValidTree = false;
            logger.error(e.getMessage(), e);
        }

    }

    /*traverse the root and returns nearest n neighbours*/
    @Override
    public List<StoreItemDTO> nearestNeighbours(Point point, int numberOfNeighbours, LocalTime localTime) throws GeoLocationException, InvalidKDTreeException {
        if (isValidTree) {
            TreeSet<DistanceStoreItem> treeStore = new TreeSet<>((DistanceStoreItem o1, DistanceStoreItem o2) -> (o1.getDistance().compareTo(o2.getDistance())));
            traverseAndGetNearestNeighbours(root, point, numberOfNeighbours, treeStore, localTime);
            //return StoreItemDTO.convertToDTO(treeStore.stream().map(distanceItem -> distanceItem.getStoreItem()));
            return StoreItemDTO.convertToDTO(treeStore.stream().map(DistanceStoreItem::getStoreItem));
        } else {
            throw new InvalidKDTreeException();
        }
    }

    /*recursive helper method to return nearest n neighbours*/
    /* keep diving deep until leaf node found */
    private void traverseAndGetNearestNeighbours(KDNode node, Point point, int numberOfNeighbours, TreeSet<DistanceStoreItem> treeStore, LocalTime localTime) throws GeoLocationException {
        Optional<KDNode> child = node.getChildFor(point);
        if (!child.isPresent()) {
            // leaf node found , take second action on algorithm
            List<StoreItem> storeItems = node.getStoreItems();
            treeStore.addAll(storeItems.stream().filter((StoreItem storeItem) ->
                    storeItem.getTodayOpen().isPresent() && storeItem.getTodayOpen().get().isBefore(localTime) && storeItem.getTodayClose().isPresent() && storeItem.getTodayClose().get().isAfter(localTime) && storeItem.getLocation().getLongitude().isPresent() && storeItem.getLocation().getLatitude().isPresent()
            ).map((StoreItem storeItem) -> {
                try {
                    return new DistanceStoreItem(storeItem, point);
                } catch (GeoLocationException e) {
                    logger.error(e.getMessage(), e);
                }
                return null;
            }).collect(Collectors.toList()));
            balanceTree(treeStore, numberOfNeighbours);
        } else {
            traverseAndGetNearestNeighbours(child.get(), point, numberOfNeighbours, treeStore, localTime);
            // after leaf node found , search for boxes in distance closer
            KDNode nextNode;
            // reference check
            if (node.getRight().isPresent() && node.getLeft().isPresent()) {
                if (node.getRight().get() == child.get()) {
                    nextNode = node.getLeft().get();
                } else {
                    nextNode = node.getRight().get();
                }
                if (treeStore.size() < numberOfNeighbours) {
                    // add other stores without distance check
                    traverseAndGetNearestNeighbours(nextNode, point, numberOfNeighbours, treeStore, localTime);
                } else {
                    // make box distance check
                    if (treeStore.last().getDistance() > nextNode.getBox().distanceTo(point)) {
                        traverseAndGetNearestNeighbours(nextNode, point, numberOfNeighbours, treeStore, localTime);
                    }
                }
            }
        }

    }


    private void balanceTree(TreeSet<DistanceStoreItem> treeStore, int numberOfNeighbours) {
        while (treeStore.size() > numberOfNeighbours) {
            treeStore.pollLast();
        }
    }

    private KDNode buildKDTree(List<StoreItem> storeList, int dimension) throws GeoLocationException {

        Double minLatitude = Double.MAX_VALUE;
        Double maxLatitude = Double.MIN_VALUE;

        Double minLongitude = Double.MAX_VALUE;
        Double maxLongitude = Double.MIN_VALUE;

        // find min max values for Box
        MutablePair<Double, Double> pair = new MutablePair<>();
        for (StoreItem storeItem : storeList) {
            Optional<Double> latitude = storeItem.getLocation().getLatitude();
            if (latitude.isPresent()) {
                checkMinMax(pair, latitude.get(), minLatitude, maxLatitude);
                minLatitude = pair.left;
                maxLatitude = pair.right;
            }
            Optional<Double> longitude = storeItem.getLocation().getLongitude();
            if (longitude.isPresent()) {
                checkMinMax(pair, longitude.get(), minLongitude, maxLongitude);
                minLongitude = pair.left;
                maxLongitude = pair.right;
            }
        }

        if (storeList.size() > Constants.STORE_IN_BOX_THRESHOLD) {

            List<StoreItem> leftNode = new ArrayList<>();
            List<StoreItem> rightNode = new ArrayList<>();

            Double middle;
            if (dimension % 2 == 1) {
                // latitude break into two parts
                middle = (minLatitude + maxLatitude) / 2;
                storeList.forEach((StoreItem storeItem) -> {
                    Optional<Double> latitude = storeItem.getLocation().getLatitude();
                    latitude.ifPresent(lat -> {
                        if (Double.compare(lat, middle) >= 0) {
                            rightNode.add(storeItem);
                        } else {
                            leftNode.add(storeItem);
                        }
                    });
                });
            } else {
                // longitude break into two parts
                middle = (minLongitude + maxLongitude) / 2;
                storeList.forEach(storeItem -> {
                    Optional<Double> longitude = storeItem.getLocation().getLongitude();
                    longitude.ifPresent(lo -> {
                        if (Double.compare(lo, middle) >= 0) {
                            rightNode.add(storeItem);
                        } else {
                            leftNode.add(storeItem);
                        }
                    });
                });
            }
            return new KDNode(buildKDTree(leftNode, dimension + 1), buildKDTree(rightNode, dimension + 1), new Box(minLatitude, maxLatitude, minLongitude, maxLongitude), dimension, null);
        } else {
            // leaf node , set store list
            return new KDNode(null, null, new Box(minLatitude, maxLatitude, minLongitude, maxLongitude), dimension, storeList);
        }
    }


    /*returns leaf nodes that holds store items used in unit test for now*/
    @Override
    public List<StoreItem> getLeafNodes() throws InvalidKDTreeException {
        if (isValidTree) {
            List<StoreItem> storeItemList = new ArrayList<>();
            traverseAndGetLeafNodes(root, storeItemList);
            return storeItemList;
        } else {
            throw new InvalidKDTreeException();
        }
    }

    /*recursive helper method that returns leaf nodes that holds store items*/

    private void traverseAndGetLeafNodes(KDNode node, List<StoreItem> storeItemList) {
        Optional<KDNode> right = node.getRight();
        Optional<KDNode> left = node.getLeft();
        if (!right.isPresent() && !left.isPresent()) {
            storeItemList.addAll(node.getStoreItems());
        } else {
            left.ifPresent(l -> traverseAndGetLeafNodes(l, storeItemList));
            right.ifPresent(r -> traverseAndGetLeafNodes(r, storeItemList));
        }
    }

    /*check binary tree structure and return result*/
    @Override
    public boolean isValidBinaryTree() {
        return isValidTree && isValidBinaryTree(root);

    }

    /*check binary tree structure and return result recursive private method*/
    private boolean isValidBinaryTree(KDNode node) {
        Optional<KDNode> right = node.getRight();
        Optional<KDNode> left = node.getLeft();
        if ((!right.isPresent() && left.isPresent()) || (right.isPresent() && !left.isPresent())) {
            return false;
        } else {
            return !right.isPresent() || !left.isPresent() || isValidBinaryTree(left.get()) && isValidBinaryTree(right.get());
        }
    }


    private void checkMinMax(MutablePair<Double, Double> pair, Double value, Double min, Double max) {
        if (Double.compare(value, min) < 0) {
            pair.setLeft(value);
        } else {
            pair.setLeft(min);
        }
        if (Double.compare(value, max) > 0) {
            pair.setRight(value);
        } else {
            pair.setRight(max);
        }
    }

    @Override
    public Integer getTotalStoreNumber() throws InvalidKDTreeException {
        if (isValidTree) {
            return totalStoreNumber;
        } else {
            throw new InvalidKDTreeException();
        }
    }

    @Override
    public List<StoreItemDTO> getAllStoreItems() throws InvalidKDTreeException {
        if (isValidTree) {
            return StoreItemDTO.convertToDTOMinimal(storeItemMap.values().stream());
        } else {
            throw new InvalidKDTreeException();
        }
    }

    @Override
    public StoreItemDTO getStoreItem(String uuid) throws InvalidKDTreeException {
        if (isValidTree) {
            return StoreItemDTO.convertToDTO(storeItemMap.get(uuid));
        } else {
            throw new InvalidKDTreeException();
        }
    }
}

