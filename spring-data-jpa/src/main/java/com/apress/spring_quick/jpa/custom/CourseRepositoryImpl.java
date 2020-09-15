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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Implementation fo the custom repository functionality declared in {@link CourseRepositoryCustom} based on JPA. To use
 * this implementation in combination with Spring Data JPA you can either register it programmatically:
 *
 * <pre>
 * EntityManager em = ... // Obtain EntityManager
 *
 * CourseRepositoryCustom custom = new CourseRepositoryImpl();
 * custom.setEntityManager(em);
 *
 * RepositoryFactorySupport factory = new JpaRepositoryFactory(em);
 * CourseRepository repository = factory.getRepository(CourseRepository.class, custom);
 * </pre>
 *
 * Using the Spring namespace the implementation will just get picked up due to the classpath scanning for
 * implementations with the {@code Impl} postfix.
 *
 * <pre>
 * &lt;jpa:repositories base-package=&quot;com.acme.repository&quot; /&gt;
 * </pre>
 *
 * If you need to manually configure the custom instance see {@link CourseRepositoryImplJdbc} for an example.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
class CourseRepositoryImpl implements CourseRepositoryCustom {

	@PersistenceContext private EntityManager em;

	/**
	 * Configure the entity manager to be used.
	 *
	 * @param em the {@link EntityManager} to set.
	 */
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/*
	 * (non-Javadoc)
	 * @see example.springdata.jpa.CourseRepositoryCustom#myCustomBatchOperation()
	 */
	public List<Course> myCustomBatchOperation() {

		CriteriaQuery<Course> criteriaQuery = em.getCriteriaBuilder().createQuery(Course.class);
		criteriaQuery.select(criteriaQuery.from(Course.class));
		return em.createQuery(criteriaQuery).getResultList();
	}
}
