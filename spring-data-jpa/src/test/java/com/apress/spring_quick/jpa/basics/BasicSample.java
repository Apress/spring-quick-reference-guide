/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licenseimport static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.example.domain.User;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
ess or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apress.spring_quick.jpa.basics;

import static org.assertj.core.api.Assertions.*;

import com.apress.spring_quick.jpa.simple.Course;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * This unit tests shows plain usage of {@link SimpleJpaRepository}.
 */
public class BasicSample {

	CrudRepository<Course, Long> userRepository;
	EntityManager em;

	/**
	 * Sets up a {@link SimpleJpaRepository} instance.
	 */
	@Before
	public void setUp() {
		// This could be simplified by using @DataJpaTest on the test class
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpa.sample.plain");
		em = factory.createEntityManager();

		userRepository = new SimpleJpaRepository<>(Course.class, em);

		em.getTransaction().begin();
	}

	@After
	public void tearDown() {
		em.getTransaction().rollback();
	}

	@Test
	public void savingUsers() {

		Course course = new Course();
		course.setName("Java 11");

		course = userRepository.save(course);

		assertThat(userRepository.findById(course.getId())).hasValue(course);
	}
}
