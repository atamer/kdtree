package com.jumbo.store.kdtree;

import com.jumbo.store.json.StoreItem;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*This class is immutable , you can not change any property after creation ,
* */
public class KDNode {

    private final KDNode left;
    private final KDNode right;
    private final Box box;

    /*dimension = 0 -> longtitude,  dimension = 1 -> latitude  * */
    private final int dimension;
    private final List<StoreItem> storeItems;

    public KDNode(KDNode left, KDNode right, Box box, int dimension, List<StoreItem> storeItems) {
        this.left = left;
        this.right = right;
        this.box = box;
        this.dimension = dimension;
        if (storeItems != null) {
            this.storeItems = Collections.unmodifiableList(storeItems);
        } else {
            this.storeItems = null;
        }
    }

    /*Check if right or left child covers point*/
    public Optional<KDNode> getChildFor(Point point) {
        if (left != null && right != null) {
            Double pointGeo;
            Double middle;
            if ((dimension % 2) == 0) {
                middle = box.getMiddleLongitude();
                pointGeo = point.getLongtitude();
            } else {
                middle = box.getMiddleLatitude();
                pointGeo = point.getLatitude();
            }
            if (pointGeo < middle) {
                return Optional.of(left);
            } else {
                return Optional.of(right);
            }
        } else {
            return Optional.empty();
        }
    }

    public List<StoreItem> getStoreItems() {
        return storeItems;
    }

    public Optional<KDNode> getLeft() {
        return Optional.ofNullable(left);
    }

    public Optional<KDNode> getRight() {
        return Optional.ofNullable(right);
    }

    public Box getBox() {
        return box;
    }


}
