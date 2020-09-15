package com.apress.springquick.springbootmvc;

import com.apress.spring_quick.jpa.simple.SimpleCourseRepository;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

/*
 * Copyright 2020, Adam L. Davis
 */

/**
 * Contributes to Actuator's /info end-point with additional info (count of courses in this case).
 */
public class CustomInfoContributor implements InfoContributor {

    final SimpleCourseRepository courseRepository;

    public CustomInfoContributor(final SimpleCourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void contribute(final Info.Builder builder) {
        builder.withDetail("count_courses", courseRepository.count());
    }
}
