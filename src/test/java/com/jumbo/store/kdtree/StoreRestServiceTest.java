package com.jumbo.store.kdtree;

import com.jumbo.store.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Macintosh on 05/11/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {"json_file_name = /json/stores1.json"})
@WebAppConfiguration
public class StoreRestServiceTest {

    private final MediaType contentTypeHal = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaTypes.HAL_JSON.getSubtype(), Charset.forName("utf8"));

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllStores() throws Exception {
        mockMvc.perform(get("/store")).andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }


    @Test
    public void getStoresItem() throws Exception {
        mockMvc.perform(get("/store/A4IKYx4XrQUAAAFIjq0YwKxK")).andExpect(status().isOk())
                .andExpect(content().contentType(contentTypeHal)).andExpect(jsonPath("uuid", is("A4IKYx4XrQUAAAFIjq0YwKxK")))
                .andExpect(jsonPath("location.longitude", is(5.862276))).andExpect(jsonPath("location.latitude", is(52.708604)))
                .andExpect(jsonPath("address.addressName", is("Jumbo Marknesse Breestraat"))).andExpect(jsonPath("address.city", is("Marknesse")))
                .andExpect(jsonPath("todayClose.hour", is(20))).andExpect(jsonPath("todayClose.minute", is(0)))
                .andExpect(jsonPath("todayOpen.hour", is(8))).andExpect(jsonPath("todayOpen.minute", is(0)));

    }


    @Test
    public void getNNStoresItem() throws Exception {
        mockMvc.perform(get("/store/neighbors?lat=52.28496235446573&lang=5.767822265625&numberOfNeighbors=5")).andExpect(status().isOk())
                .andExpect(content().contentType(contentType)).andExpect(jsonPath("$[0].uuid", is("EqIKYx4X4O0AAAFIMY0YwKxK")))
                .andExpect(jsonPath("$[1].uuid", is("kNEKYx4XtMQAAAFIL6YYwKxK"))).andExpect(jsonPath("$[2].uuid", is("3u0KYx4XlCAAAAFJ3QsYZ4CR")))
                .andExpect(jsonPath("$[3].uuid", is("XNsKYx4XolwAAAFN9CQ7frI2"))).andExpect(jsonPath("$[4].uuid", is("JYYKYx4XC1oAAAFItvcYwKxJ")));
    }
}
