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
package com.apress.spring_quick.jpa.custom;

import com.apress.spring_quick.jpa.simple.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test showing the basic usage of {@link CourseRepository}.
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
// @ActiveProfiles("jdbc") // Uncomment @ActiveProfiles to enable the JDBC Implementation of the custom repository
@EntityScan(basePackages = "com.apress.spring_quick.jpa.simple")
public class CourseRepositoryCustomizationTests {

    @Autowired
    CourseRepository repository;

    /**
     * Tests inserting a user and asserts it can be loaded again.
     */
    @Test
    public void testInsert() {

        Course course = new Course();
        course.setName("name");

        course = repository.save(course);

        assertThat(repository.findById(course.getId())).hasValue(course);
    }

    @Test
    public void saveAndFindByLastNameAndFindByName() {

        Course course = new Course();
        course.setName("foobar");
        course.setDescription("lastname");

        course = repository.save(course);

        List<Course> courses = repository.findByDescription("lastname");

        assertThat(courses).contains(course);
        assertThat(course).isEqualTo(repository.findByTheName("foobar"));
    }

    /**
     * Test invocation of custom method.
     */
    @Test
    public void testCustomMethod() {

        Course course = new Course();
        course.setName("name");

        course = repository.save(course);

        List<Course> courses = repository.myCustomBatchOperation();

        assertThat(courses).contains(course);
    }
}
