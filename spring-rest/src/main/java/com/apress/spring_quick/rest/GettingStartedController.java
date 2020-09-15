package com.apress.spring_quick.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/*
 * Copyright 2020, Adam L. Davis
 */
@RestController
public class GettingStartedController {
    @GetMapping("/")
    public EntityModel<Customer> getCustomer() {
        return EntityModel.of(new Customer("John", "Doe"))
                .add(linkTo(GettingStartedController.class).withSelfRel())
                .add(linkTo(GettingStartedController.class)
                        .slash("next").withRel("next"));
    }
}
