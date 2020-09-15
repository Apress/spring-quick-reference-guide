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

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Same as {@link SimpleCourseRepository} except with @Transactional annotations.
 */
// timeout of ten seconds, always create a new Transaction (suspending any current), and highest isolation level
@Transactional(timeout = 10, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
@Repository
public interface TransactionalCourseRepository extends CrudRepository<Course, Long> {

    /**
     * Find the course with the given name. This method will be translated into a query using the
     * {@link javax.persistence.NamedQuery} annotation at the {@link Course} class.
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    Course findByTheName(String name);

    /**
     * Uses {@link Optional} as return and parameter type.
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    Optional<Course> findByName(Optional<String> name);

    /**
     * Find all courses with the given description. This method will be translated into a query by constructing it directly
     * from the method name as there is no other query declared.
     *
     * @param description
     * @return
     */
    @Transactional(readOnly = true)
    List<Course> findByDescription(String description);

    /**
     * Returns all courses with the given subtitle. This method will be translated into a query using the one declared in
     * the {@link Query} annotation declared one.
     *
     * @param subtitle
     * @return
     */
    @Transactional(readOnly = true)
    @Query("select u from Course u where u.subtitle = :subtitle")
    List<Course> findBySubtitle(String subtitle);

    /**
     * Returns all courses with the given name as first- or description. This makes the query to method relation much more
     * refactoring-safe as the order of the method parameters is completely irrelevant.
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    @Query("select u from Course u where u.subtitle = :name or u.description = :name")
    List<Course> findBySubtitleOrDescription(String name);

    /**
     * Returns the total number of entries deleted as their descriptions match the given one.
     *
     * @param description
     * @return
     */
    Long removeByDescription(String description);

    /**
     * Returns a {@link Slice} counting a maximum number of {@link Pageable#getPageSize()} courses matching given criteria
     * starting at {@link Pageable#getOffset()} without prior count of the total number of elements available.
     *
     * @param description
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    Slice<Course> findByDescriptionOrderByNameAsc(String description, Pageable page);

    /**
     * Return the first 2 courses ordered by their description asc.
     *
     * <pre>
     * Example for findFirstK / findTopK functionality.
     * </pre>
     *
     * @return
     */
    @Transactional(readOnly = true)
    List<Course> findFirst2ByOrderByDescriptionAsc();

    /**
     * Return the first 2 courses ordered by the given {@code sort} definition.
     *
     * <pre>
     * This variant is very flexible because one can ask for the first K results when a ASC ordering
     * is used as well as for the last K results when a DESC ordering is used.
     * </pre>
     *
     * @param sort
     * @return
     */
    @Transactional(readOnly = true)
    List<Course> findTop2By(Sort sort);

    /**
     * Return all the courses with the given subtitle or description. Makes use of SpEL (Spring Expression Language).
     *
     * @param course
     * @return
     */
    @Transactional(readOnly = true)
    @Query("select u from Course u where u.subtitle = :#{#course.subtitle} or u.description = :#{#course.description}")
    Iterable<Course> findBySubtitleOrDescription(Course course);
}
