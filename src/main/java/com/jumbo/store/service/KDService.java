package com.jumbo.store.service;

import com.jumbo.store.dto.StoreItemDTO;
import com.jumbo.store.exception.GeoLocationException;
import com.jumbo.store.exception.InvalidKDTreeException;
import com.jumbo.store.json.StoreItem;
import com.jumbo.store.kdtree.Point;

import java.time.LocalTime;
import java.util.List;

public interface KDService {

    List<StoreItem> getLeafNodes() throws InvalidKDTreeException;

    boolean isValidBinaryTree();

    Integer getTotalStoreNumber() throws InvalidKDTreeException;

    List<StoreItemDTO> nearestNeighbours(Point point, int numberOfNeighbours, LocalTime localTime) throws GeoLocationException, InvalidKDTreeException;

    List<StoreItemDTO> getAllStoreItems() throws InvalidKDTreeException;

    StoreItemDTO getStoreItem(String uuid) throws InvalidKDTreeException;

}
