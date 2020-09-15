/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apress.spring_quick.jpa.projections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.projection.TargetAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link CustomerRepository} to show projection capabilities.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerRepositoryIntegrationTest {

    @Configuration
    @EnableAutoConfiguration
    static class Config {
    }

    @Autowired
    CustomerRepository customers;

    Customer george, jim;

    @Before
    public void setUp() {
        this.george = customers.save(new Customer("George", "Washington"));
        this.jim = customers.save(new Customer("Jimmy", "Carter"));
    }
    @After
    public void deleteAll() {
        customers.deleteAll();
    }

    @Test
    public void projectsEntityIntoInterface() {

        assertThat(customers.findAllProjectedBy())
                .hasSize(2)
                .first().satisfies(it -> assertThat(it.getFirstname()).isEqualTo("George"));
    }

    @Test
    public void projectsMapIntoInterface() {

        assertThat(customers.findsByProjectedColumns())
                .hasSize(2)
                .first().satisfies(it -> assertThat(it.getFirstname()).isEqualTo("George"));

    }

    @Test
    public void projectsToDto() {
        assertThat(customers.findAllDtoedBy())
                .hasSize(2)
                .first().satisfies(it -> assertThat(it.getFirstname()).isEqualTo("George"));
    }

    @Test
    public void projectsDynamically() {
        assertThat(customers.findByFirstname("George", CustomerProjection.class))
                .hasSize(1)
                .first()
                .satisfies(it -> assertThat(it.getFirstname()).isEqualTo("George"));
    }

    @Test
    public void projectsIndividualDynamically() {

        CustomerSummary result = customers.findProjectedById(george.getId(), CustomerSummary.class);

        assertThat(result.getFullName()).isEqualTo("George Washington");

        // Proxy backed by original instance as the projection uses dynamic elements
        assertThat(result).isInstanceOfSatisfying(TargetAware.class,
                it -> assertThat(it.getTarget()).isInstanceOf(Customer.class));
    }

    @Test
    public void projectIndividualInstance() {

        CustomerProjection projectedDave = customers.findProjectedById(george.getId());

        assertThat(projectedDave.getFirstname()).isEqualTo("George");
        assertThat(projectedDave).isInstanceOfSatisfying(TargetAware.class,
                it -> assertThat(it.getTarget()).isInstanceOf(Map.class));
    }

    @Test
    public void supportsProjectionInCombinationWithPagination() {

        Page<CustomerProjection> page = customers
                .findPagedProjectedBy(PageRequest.of(0, 1, Sort.by(Direction.ASC, "lastname")));

        assertThat(page.getContent().get(0).getFirstname()).isEqualTo("Jimmy");
    }

    @Test
    public void appliesProjectionToOptional() {
        assertThat(customers.findOptionalProjectionByLastname("Carter")).isPresent();
    }
}
