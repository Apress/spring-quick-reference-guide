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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository interface for {@link Course} instances. Provides basic CRUD operations due to the extension of
 * {@link JpaRepository}. Includes custom implemented functionality by extending {@link CourseRepositoryCustom}.
 */
public interface CourseRepository extends CrudRepository<Course, Long>, CourseRepositoryCustom {

    /**
     * Find the course with the given name. This method will be translated into a query using the
     * {@link javax.persistence.NamedQuery} annotation at the {@link Course} class.
     *
     * @param name
     * @return
     */
    Course findByTheName(String name);

    /**
     * Find all courses with the given description. This method will be translated into a query by constructing it directly
     * from the method name as there is no other query declared.
     *
     * @param description
     * @return
     */
    List<Course> findByDescription(String description);

    /**
     * Returns all courses with the given subtitle. This method will be translated into a query using the one declared in
     * the {@link Query} annotation.
     *
     * @param subtitle
     * @return
     */
    @Query("select u from Course u where u.subtitle = :subtitle")
    List<Course> findBySubtitle(String subtitle);
}
