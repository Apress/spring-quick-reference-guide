/*
 * Copyright 2014-2018 the original author or authors.
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
package com.apress.spring_quick.jpa.auditing;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class AuditableCourseSample {

	@Autowired AuditableUserRepository repository;
	@Autowired AuditorAwareImpl auditorAware;
	@Autowired AuditingEntityListener listener;

	@Ignore //TODO Fix
	@Test
	public void auditEntityCreation() throws Exception {

		assertThat(ReflectionTestUtils.getField(listener, "handler"), is(notNullValue()));

		AuditableUser user = new AuditableUser();
		user.setUsername("username");

		user = repository.save(user);
		user = repository.save(user);

		assertThat(user.getCreatedBy(), CoreMatchers.is(user));
		assertThat(user.getLastModifiedBy(), CoreMatchers.is(user));
	}
}
