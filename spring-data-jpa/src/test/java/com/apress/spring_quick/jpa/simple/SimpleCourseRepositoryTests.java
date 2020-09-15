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
package com.apress.spring_quick.jpa.simple;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Integration test showing the basic usage of {@link SimpleCourseRepository}.
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class SimpleCourseRepositoryTests {

    @Autowired
    SimpleCourseRepository repository;
    Course course;

    @Before
    public void setUp() {

        course = new Course();
        course.setName("foobar");
        course.setSubtitle("subtitle");
        course.setDescription("description");
    }

    @Test
    public void findSavedUserById() {

        course = repository.save(course);

        assertThat(repository.findById(course.getId())).hasValue(course);
    }

    @Test
    public void findSavedUserByDescription() throws Exception {

        course = repository.save(course);

        assertThat(repository.findByDescription("description")).contains(course);
    }

    @Test
    public void findBySubtitleOrDescription() throws Exception {

        course = repository.save(course);

        assertThat(repository.findBySubtitleOrDescription("description")).contains(course);
    }

    @Test
    public void useOptionalAsReturnAndParameterType() {

        assertThat(repository.findByName(Optional.of("foobar"))).isEmpty();

        repository.save(course);

        assertThat(repository.findByName(Optional.of("foobar"))).isPresent();
    }

    @Test
    public void removeByDescription() {

        // create a 2nd user with the same description as user
        Course course2 = new Course();
        course2.setDescription(course.getDescription());

        // create a 3rd user as control group
        Course course3 = new Course();
        course3.setDescription("no-positive-match");

        repository.saveAll(Arrays.asList(course, course2, course3));

        assertThat(repository.removeByDescription(course.getDescription())).isEqualTo(2L);
        assertThat(repository.existsById(course3.getId())).isTrue();
    }

    @Test
    public void useSliceToLoadContent() {

        repository.deleteAll();

        // int repository with some values that can be ordered
        int totalNumber = 11;
        List<Course> source = new ArrayList<>(totalNumber);

        for (int i = 1; i <= totalNumber; i++) {
            Course course = new Course();
            course.setDescription(this.course.getDescription());
            course.setName(course.getDescription() + "-" + String.format("%03d", i));
            source.add(course);
        }

        repository.saveAll(source);

        Slice<Course> users = repository.findByDescriptionOrderByNameAsc(this.course.getDescription(), PageRequest.of(1, 5));

        assertThat(users).containsAll(source.subList(5, 10));
    }

    @Test
    public void findFirst2ByOrderByDescriptionAsc() {

        Course course0 = new Course();
        course0.setDescription("description-0");

        Course course1 = new Course();
        course1.setDescription("description-1");

        Course course2 = new Course();
        course2.setDescription("description-2");

        // we deliberately save the items in reverse
        repository.saveAll(Arrays.asList(course2, course1, course0));

        List<Course> result = repository.findFirst2ByOrderByDescriptionAsc();

        assertThat(result.size(), is(2));
        assertThat(result, hasItems(course0, course1));
    }

    @Test
    public void findTop2ByWithSort() {

        Course course0 = new Course();
        course0.setDescription("description-0");

        Course course1 = new Course();
        course1.setDescription("description-1");

        Course course2 = new Course();
        course2.setDescription("description-2");

        // we deliberately save the items in reverse
        repository.saveAll(Arrays.asList(course2, course1, course0));

        List<Course> resultAsc = repository.findTop2By(Sort.by(ASC, "description"));

        assertThat(resultAsc.size(), is(2));
        assertThat(resultAsc, hasItems(course0, course1));

        List<Course> resultDesc = repository.findTop2By(Sort.by(DESC, "description"));

        assertThat(resultDesc.size(), is(2));
        assertThat(resultDesc, hasItems(course1, course2));
    }

    @Test
    public void findBySubtitleOrDescriptionUsingSpEL() {

        Course first = new Course();
        first.setDescription("description");

        Course second = new Course();
        second.setSubtitle("subtitle");

        Course third = new Course();

        repository.saveAll(Arrays.asList(first, second, third));

        Course reference = new Course();
        reference.setSubtitle("subtitle");
        reference.setDescription("description");

        Iterable<Course> users = repository.findBySubtitleOrDescription(reference);

        assertThat(users, is(iterableWithSize(2)));
        assertThat(users, hasItems(first, second));
    }
}
