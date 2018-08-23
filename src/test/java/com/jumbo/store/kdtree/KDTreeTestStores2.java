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
@TestPropertySource(properties = {"json_file_name = /json/stores2.json"})
public class KDTreeTestStores2 {

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

        List<String> expectedResult = Arrays.asList("cg0KYx4XhfUAAAFI3dMYwKxJ", "tY0KYx4X6VYAAAFI.3oYwKxK", "te0KYx4XWzAAAAFIJIoYwKxK");
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

        List<String> expectedResult = Arrays.asList("hyoKYx4XnsAAAAFIsicYwKxK", "7oQKYx4X5fEAAAFIIT4YwKxK", "a5gKYx4XrmwAAAFIOS4YwKxK");
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

        List<String> expectedResult = Arrays.asList("7oQKYx4X5fEAAAFIIT4YwKxK", "hyoKYx4XnsAAAAFIsicYwKxK", "a5gKYx4XrmwAAAFIOS4YwKxK", "iNcKYx4XORAAAAFICQIYwKxK", "WXIKYx4XBRYAAAFIIgoYwKxK");
        Collections.sort(expectedResult);

        List<String> uuidList = nearestPoints.stream().map(storeItemDTO -> storeItemDTO.getUuid()).collect(Collectors.toList());
        Collections.sort(uuidList);
        assertEquals(true, expectedResult.equals(uuidList));

    }

    @Test
    public void kdTreeSearchSmallJson() throws Exception {

        Point point = new Point(52.98833725339541, 6.2896728515625);

        List<StoreItemDTO> nearestPoints = kdService.nearestNeighbours(point, 5, LocalTime.of(15, 45));
        assertEquals(5, nearestPoints.size());

        List<String> expectedResult = Arrays.asList("7oQKYx4X5fEAAAFIIT4YwKxK", "hyoKYx4XnsAAAAFIsicYwKxK", "a5gKYx4XrmwAAAFIOS4YwKxK", "iNcKYx4XORAAAAFICQIYwKxK", "WXIKYx4XBRYAAAFIIgoYwKxK");
        Collections.sort(expectedResult);

        List<String> uuidList = nearestPoints.stream().map(storeItemDTO -> storeItemDTO.getUuid()).collect(Collectors.toList());
        Collections.sort(uuidList);
        assertEquals(true, expectedResult.equals(uuidList));

    }


    @Test(expected = IllegalArgumentException.class)
    public void kdTreeSearchException() throws Exception {

        Point point = new Point(52999.98833725339541, 6.2896728515625);

        List<StoreItemDTO> nearestPoints = kdService.nearestNeighbours(point, 5, LocalTime.of(15, 45));
        assertEquals(5, nearestPoints.size());

        List<String> expectedResult = Arrays.asList("7oQKYx4X5fEAAAFIIT4YwKxK", "hyoKYx4XnsAAAAFIsicYwKxK", "a5gKYx4XrmwAAAFIOS4YwKxK", "iNcKYx4XORAAAAFICQIYwKxK", "WXIKYx4XBRYAAAFIIgoYwKxK");
        Collections.sort(expectedResult);

        List<String> uuidList = nearestPoints.stream().map(storeItemDTO -> storeItemDTO.getUuid()).collect(Collectors.toList());
        Collections.sort(uuidList);
        assertEquals(true, expectedResult.equals(uuidList));

    }

}
