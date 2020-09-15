package com.apress.spring_quick.autoconfig;

import com.google.common.collect.ImmutableList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Copyright 2020, Adam L. Davis
 */

/**
 * Demonstrates using Autoconfiguration in a Spring application (see "spring.factories").
 */
// multiple Conditional* annotations can be used
@ConditionalOnClass(ImmutableList.class) //<-- This Configuration will not load if this class is not present
@ConditionalOnMissingBean(Library.class) //<-- This Configuration will not load if this bean is already defined
@Configuration
public class AutoConfig {

    @ConditionalOnMissingBean(Library.class) //<-- Can also go here
    @ConditionalOnProperty(name = "library.enabled", matchIfMissing = true)
    @Bean
    public Library library() {
        return new Library();
    }
}
