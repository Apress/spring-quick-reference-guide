package com.apress.springquick.springbootmvc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/*
 * Copyright 2020, Adam L. Davis
 */
@JsonDeserialize
@JsonSerialize
public class CourseDto {

    final Long id;
    final String title;
    final String subtitle;
    final String description;

    public CourseDto(Long id, String title, String subtitle, String description) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }
}
