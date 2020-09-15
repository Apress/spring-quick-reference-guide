package com.apress.springquick.springbootmvc;

import com.apress.spring_quick.jpa.simple.Course;
import com.apress.spring_quick.jpa.simple.SimpleCourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/*
 * Copyright 2020, Adam L. Davis
 */

/**
 * REST controller which consumes and produces JSON.
 */
@Transactional
@RestController
@RequestMapping(value = "/api/v1",  //<-- every URL gets prefixed with this
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseRestController {

    final SimpleCourseRepository simpleCourseRepository;

    public CourseRestController(SimpleCourseRepository simpleCourseRepository) {
        this.simpleCourseRepository = simpleCourseRepository;
    }

    @GetMapping("/courses")
    //@ResponseBody implied because of @RestController
    public List<CourseDto> list() {
        final Iterator<Course> iterator = simpleCourseRepository.findAll().iterator();
        final List<CourseDto> dtos = new ArrayList<>();
        iterator.forEachRemaining(course ->
                dtos.add(new CourseDto(course.getId(), course.getName(), course.getSubtitle(), course.getDescription())));

        return dtos;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/courses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody final CourseDto course) {
        Course entity = new Course();
        entity.setName(course.getTitle());
        entity.setSubtitle(course.getSubtitle());
        entity.setDescription(course.getDescription());
        simpleCourseRepository.save(entity);
    }

    @GetMapping("/courses/{id:\\d+}")
    public CourseDto course(@PathVariable final Long id) { //<-- name matched from parameter name, id
        return simpleCourseRepository.findById(id)
                .map(course -> new CourseDto(course.getId(), course.getName(), course.getSubtitle(), course.getDescription()))
                .orElseThrow(() -> new IllegalArgumentException("No course with given id " + id));
    }

    @GetMapping("/courses/name:{name}")
    public CourseDto course(@PathVariable final String name) { //<-- name matched from parameter name
        return Optional.ofNullable(simpleCourseRepository.findByTheName(name))
                .map(course -> new CourseDto(course.getId(), course.getName(), course.getSubtitle(), course.getDescription()))
                .orElseThrow(() -> new IllegalArgumentException("No course with given name, " + name));
    }

    @DeleteMapping("/courses/{id}")
    public void delete(@PathVariable final Long id) { //<-- name matched from parameter name
        simpleCourseRepository.deleteById(id);
    }

}
