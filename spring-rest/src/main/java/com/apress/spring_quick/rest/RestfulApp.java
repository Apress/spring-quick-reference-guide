package com.apress.spring_quick.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/*
 * Copyright 2020, Adam L. Davis
 */
@EnableWebMvc
@SpringBootApplication
public class RestfulApp {
    public static void main(String[] args) {
        SpringApplication.run(RestfulApp.class, args);
    }
}
