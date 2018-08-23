package com.jumbo.store.validator;

import com.jumbo.store.json.StoreItem;

public interface StoreValidator {


    boolean isValid(StoreItem storeItem);
}
