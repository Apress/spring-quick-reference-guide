package com.apress.spring_quick.mobile;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * Copyright 2020, Adam L. Davis
 */
@Configuration
@EnableJpaRepositories(basePackages =
        {"com.apress.spring_quick.jpa.simple", "com.apress.spring_quick.jpa.compositions"},
        enableDefaultTransactions = true)
@ComponentScan(basePackages = {"com.apress.spring_quick.jpa.simple", "com.apress.spring_quick.jpa.compositions"})
public class ServiceConfig {
}
