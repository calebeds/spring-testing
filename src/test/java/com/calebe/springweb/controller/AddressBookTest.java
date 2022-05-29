package com.calebe.springweb.controller;

import com.calebe.springweb.configuration.Config;
import com.calebe.springweb.service.AddressDeleter;
import com.calebe.springweb.service.AddressRetriever;
import com.calebe.springweb.service.AddressStorer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AddressBookTest.TestConfig.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressBookTest {
    @Configuration
    @ComponentScan(basePackageClasses = Config.class)
    public static class TestConfig {
        private Map<String, String> addresses = new HashMap<>();

        @Bean
        public AddressRetriever retriever() {//When called it will return a get from the HashMap
            return addresses::get;
        }

        @Bean
        public AddressStorer store() {//When called it will return a put from the HashMap
            return addresses::put;
        }

        @Bean
        public AddressDeleter delete() {
            return addresses::remove;
        }
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AddressStorer storer;

    private MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void unknownIsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/address/Tom Cruise"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void withKnownPersonAddressIsFound() throws Exception {
        storer.storeAddress("Harold", "1 King Place");


        mockMvc.perform(MockMvcRequestBuilders.get("/address/Harold"))
                .andExpect(status().isOk());
    }

    @Test
    public void withKnownPersonAddressIsCorrect() throws Exception {
        storer.storeAddress("Harold", "1 King Place");


        mockMvc.perform(MockMvcRequestBuilders.get("/address/Harold"))
                .andExpect(content().string("1 King Place"));
    }

    @Test
    public void afterAddingAddressItCanbeFound() throws Exception {
        mockMvc.perform(post("/address/Maud").content("Maud's House").contentType("application/text"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/address/Maud"))
                .andExpect(status().isOk())
                .andExpect(content().string("Maud's House"));
    }

    @Test
    public void afterAddingAnAddressItCanBeDeleted() throws Exception {
        mockMvc.perform(post("/address/Maud").content("Maud's House").contentType("application/text"))
                .andExpect(status().isOk());//Post it

        mockMvc.perform(get("/address/Maud"))
                .andExpect(status().isOk());//Get it

        mockMvc.perform(delete("/address/Maud"))
                .andExpect(status().isOk());//Delete it

        mockMvc.perform(get("/address/Maud"))
                .andExpect(status().isNotFound());//Now when make a get request, nothing is found
    }
}