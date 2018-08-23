package com.jumbo.store.kdtree;

import com.jumbo.store.Application;
import com.jumbo.store.exception.InvalidKDTreeException;
import com.jumbo.store.service.KDService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Macintosh on 05/11/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {"json_file_name = /json/invalidstores.json"})
public class InvalidKDTreeTest {

    @Autowired
    private KDService kdService;


    @Test(expected = InvalidKDTreeException.class)
    public void kdTreeConstructionCheckLeadNodes() throws Exception {
        assertEquals(kdService.getTotalStoreNumber().intValue(), kdService.getLeafNodes().size());
    }
}
