package com.calebe.springweb;

import com.calebe.springweb.configuration.Config;
import com.calebe.springweb.service.AddressRetriever;
import com.calebe.springweb.service.AddressStorer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AddressBookTest.TestConfig.class)
@WebAppConfiguration
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
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

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
}
