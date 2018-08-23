package com.jumbo.store.kdtree;

import com.jumbo.store.Application;
import com.jumbo.store.dto.StoreItemDTO;
import com.jumbo.store.service.KDService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {"json_file_name = /json/stores1.json"})
public class KDTreeTestStores1 {

    @Autowired
    private KDService kdService;


    @Test
    public void kdTreeConstructionCheckLeadNodes() throws Exception {
        assertEquals(kdService.getTotalStoreNumber().intValue(), kdService.getLeafNodes().size());
    }

    @Test
    public void kdTreeCheckBinaryStructure() throws Exception {
        assertEquals(true, kdService.isValidBinaryTree());
    }


    @Test
    public void kdTreeSearchEarlyTime() throws Exception {

        Point point = new Point(52.185990, 5.398059);

        List<StoreItemDTO> nearestPoints = kdService.nearestNeighbours(point, 3, LocalTime.of(10, 30));
        assertEquals(3, nearestPoints.size());

        List<String> expectedResult = Arrays.asList("iiIKYx4XwVgAAAFMzechgUnv", "9_wKYx4XGYkAAAFILMQYwKxJ", "3kIKYx4XYocAAAFNDH47frdc");
        Collections.sort(expectedResult);

        List<String> uuidList = nearestPoints.stream().map(storeItemDTO -> storeItemDTO.getUuid()).collect(Collectors.toList());
        Collections.sort(uuidList);
        assertEquals(true, expectedResult.equals(uuidList));

    }


    @Test
    public void kdTreeSearchLateTime() throws Exception {

        Point point = new Point(52.912214760778674, 6.2896728515625);

        List<StoreItemDTO> nearestPoints = kdService.nearestNeighbours(point, 3, LocalTime.of(20, 30));
        assertEquals(3, nearestPoints.size());

        List<String> expectedResult = Arrays.asList("PCgKYx4Xx4QAAAFISgcYwKxK", "9bIKYx4X3wgAAAFIRv0YwKxJ", "XnoKYx4Xz5sAAAFNVwI7fttp");
        Collections.sort(expectedResult);

        List<String> uuidList = nearestPoints.stream().map(storeItemDTO -> storeItemDTO.getUuid()).collect(Collectors.toList());
        Collections.sort(uuidList);
        assertEquals(true, expectedResult.equals(uuidList));

    }


    @Test
    public void kdTreeSearchBusyTime() throws Exception {

        Point point = new Point(52.98833725339541, 6.2896728515625);

        List<StoreItemDTO> nearestPoints = kdService.nearestNeighbours(point, 5, LocalTime.of(15, 45));
        assertEquals(5, nearestPoints.size());

        List<String> expectedResult = Arrays.asList("PCgKYx4Xx4QAAAFISgcYwKxK", "ieEKYx4XPfMAAAFKD5JPW5Zz", "ZhAKYx4XaVQAAAFcyDlerHrB", "sGQKYx4XT5kAAAFNg.lmVAMd", "xTIKYx4XmOUAAAFIYYAYwKxK");
        Collections.sort(expectedResult);

        List<String> uuidList = nearestPoints.stream().map(storeItemDTO -> storeItemDTO.getUuid()).collect(Collectors.toList());
        Collections.sort(uuidList);
        assertEquals(true, expectedResult.equals(uuidList));

    }


}
