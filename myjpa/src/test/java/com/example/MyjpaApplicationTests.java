package com.example;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyjpaApplication.class)
@WebAppConfiguration
public class MyjpaApplicationTests {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private Item item;
    private String label = "testLabel";
    
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.inventoryRepository.deleteAll();
        Date dt = new Date();
        this.item = this.inventoryRepository.save(new Item(label, MyjpaApplication.getExpirationDate(1), "testType") );
    }   
    
    @Test
    public void createItem() throws Exception {
        String itemJson = json(new Item("newLabel", MyjpaApplication.getExpirationDate(2), "newType") );
        this.mockMvc.perform(post("/")
                .contentType(contentType)
                .content(itemJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteItem() throws Exception {
        String itemJson = json(new Item("newLabel", MyjpaApplication.getExpirationDate(2), "newType") );
        this.mockMvc.perform(delete("/"+label)
                .contentType(contentType))
                .andExpect(status().isNoContent());
    	
    }
    @Test
    public void labelNotFoundException() throws Exception {
        mockMvc.perform(delete("/badLabel")
                .content(this.json(new Item()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
