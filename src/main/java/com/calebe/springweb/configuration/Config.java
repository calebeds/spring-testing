package com.calebe.springweb.configuration;


import com.calebe.springweb.controller.AddressController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = "com.calebe.springweb.controller")
public class Config {
}
