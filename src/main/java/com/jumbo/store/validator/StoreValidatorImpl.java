package com.jumbo.store.validator;

import com.jumbo.store.json.StoreItem;
import org.springframework.stereotype.Service;

@Service
public class StoreValidatorImpl implements StoreValidator {


    @Override
    public boolean isValid(StoreItem storeItem) {
        // check geo location and open-close dates
        // can extend the validation criteria
        if (storeItem.getLocation() != null && storeItem.getLocation().getLatitude().isPresent() && storeItem.getLocation().getLongitude().isPresent()) {
            if (storeItem.getTodayClose().isPresent() && storeItem.getTodayOpen().isPresent()) {
                if (storeItem.getUuid().isPresent()) {
                    return true;
                }
            }
        }
        return false;
    }
}
