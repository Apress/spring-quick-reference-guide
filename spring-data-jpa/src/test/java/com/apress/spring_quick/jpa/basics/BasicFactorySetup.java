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
package com.apress.spring_quick.jpa.basics;

import com.apress.spring_quick.jpa.simple.Course;
import com.apress.spring_quick.jpa.simple.SimpleCourseRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

/**
 * Test case showing how to use the basic {@link EntityManagerFactory}
 */
public class BasicFactorySetup {

	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpa.sample.plain");

	private SimpleCourseRepository userRepository;
	private EntityManager em;

	private Course course;

	/**
	 * Creates a {@link SimpleCourseRepository} instance.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() {

		em = factory.createEntityManager();

		userRepository = new JpaRepositoryFactory(em).getRepository(SimpleCourseRepository.class);

		em.getTransaction().begin();

		course = new Course();
		course.setName("name");
		course.setSubtitle("subtitle");
		course.setDescription("description");

		course = userRepository.save(course);
	}

	/**
	 * Rollback transaction.
	 */
	@After
	public void tearDown() {
		em.getTransaction().rollback();
	}

	/**
	 * Showing invocation of finder method.
	 */
	@Test
	public void executingFinders() {
		assertThat(userRepository.findByTheName("name"), Matchers.is(course));
		assertThat(userRepository.findByDescription("description").get(0), Matchers.is(course));
		assertThat(userRepository.findBySubtitle("subtitle").get(0), Matchers.is(course));
	}
}
