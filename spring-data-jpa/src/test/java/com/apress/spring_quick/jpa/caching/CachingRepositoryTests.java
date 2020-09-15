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

import com.apress.spring_quick.jpa.simple.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test to show how to use {@link Cacheable} with a Spring Data repository.
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@EntityScan(basePackages = "com.apress.spring_quick.jpa.simple")
public class CachingRepositoryTests {

    @Autowired
    CachingCourseRepository repository;
    @Autowired
    CacheManager cacheManager;

    @Test
    public void checkCachedValue() {

        Course dave = new Course();
        dave.setName("java 11");

        dave = repository.save(dave);

        assertThat(repository.findByName("java 11")).isEqualTo(dave);

        // Verify entity cached
        Cache cache = cacheManager.getCache("courseByName");
        assertThat(cache.get("java 11").get()).isEqualTo(dave);
    }

    @Test
    public void checkCacheEviction() {

        Course dave = new Course();
        dave.setName("java 11");
        repository.save(dave);

        // Verify entity evicted on cache
        Cache cache = cacheManager.getCache("courseByName");
        assertThat(cache.get("java 11")).isEqualTo(null);
    }
}
