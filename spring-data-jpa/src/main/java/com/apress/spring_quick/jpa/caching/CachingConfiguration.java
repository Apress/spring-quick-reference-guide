/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apress.spring_quick.jpa.caching;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * Java config to use Spring Data JPA with Spring caching support.
 */
@EnableCaching
@SpringBootApplication
class CachingConfiguration {

    @Bean
    public Cache courseByName() {
        return new ConcurrentMapCache("courseByName");
    }

    @Bean
    public CacheManager cacheManager(@Qualifier("courseByName") final Cache courseByName) {
        SimpleCacheManager manager = new SimpleCacheManager();

        manager.setCaches(Arrays.asList(courseByName));

        return manager;
    }
}
