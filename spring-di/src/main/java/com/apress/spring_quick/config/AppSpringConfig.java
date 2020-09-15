package com.apress.spring_quick.config;

import com.apress.spring_quick.di.MyBean;
import com.apress.spring_quick.di.MyBeanInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/*
 * Copyright 2020, Adam L. Davis
 */

/**
 * Demo of using Spring's @Configuration.
 */
@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = {"com.apress.spring_quick.di.guava"})
public class AppSpringConfig {

    @Bean
    public MyBeanInterface myBeanInterface() {
        return new MyBean();
    }

    // using Environment is one way to get property values
    @Bean
    public String name1(Environment environment) { // Spring will attempt to inject any method parameters by type
        return environment.getProperty("project.name");
    }

    // using @Value is another way to get at property values
    @Bean
    public String name2(@Value("${project.name}") final String name) {
        return name;
    }

    @Bean
    public String name(@Qualifier("name2") final String name) {
        // when two or more beans can be matched by type, use @Qualifier to specify which one
        // another way is to mark one of the beans with @Primary
        System.out.println("Application name = " + name);
        return name;
    }

}
