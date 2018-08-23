package com.jumbo.store.rest;


import com.jumbo.store.dto.StoreItemDTO;
import com.jumbo.store.exception.GeoLocationException;
import com.jumbo.store.exception.InvalidKDTreeException;
import com.jumbo.store.kdtree.Point;
import com.jumbo.store.service.KDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store")
public class StoreRestService {

    @Autowired
    private KDService kdService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<StoreItemDTO>> getAllStoreItems() throws InvalidKDTreeException {
        return ResponseEntity.ok(kdService.getAllStoreItems().stream().map(storeItemDTO -> {
            Link selfLink = ControllerLinkBuilder.linkTo(StoreRestService.class).slash(storeItemDTO.getUuid()).withSelfRel();
            storeItemDTO.add(selfLink);
            return storeItemDTO;
        }).collect(Collectors.toList()));

    }

    @RequestMapping(value = "/neighbors", method = RequestMethod.GET)
    public ResponseEntity<List<StoreItemDTO>> getNearestNeighbors(Double lang, Double lat, int numberOfNeighbors) throws GeoLocationException, InvalidKDTreeException {
        return ResponseEntity.ok(kdService.nearestNeighbours(new Point(lat, lang), numberOfNeighbors, LocalTime.now()).stream().map(storeItemDTO -> {
            Link selfLink = ControllerLinkBuilder.linkTo(StoreRestService.class).slash(storeItemDTO.getUuid()).withSelfRel();
            storeItemDTO.add(selfLink);
            return storeItemDTO;
        }).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{uuid:.+}", method = RequestMethod.GET)
    public ResponseEntity<StoreItemDTO> getStoreItem(@PathVariable("uuid") String uuid) throws InvalidKDTreeException {
        StoreItemDTO storeItemDTO = kdService.getStoreItem(uuid);
        Link selfLink = ControllerLinkBuilder.linkTo(StoreRestService.class).slash(storeItemDTO.getUuid()).withSelfRel();
        storeItemDTO.add(selfLink);
        return ResponseEntity.ok(storeItemDTO);
    }

}
