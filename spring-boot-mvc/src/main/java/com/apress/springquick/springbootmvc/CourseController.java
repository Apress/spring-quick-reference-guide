package com.apress.springquick.springbootmvc;

import com.apress.spring_quick.jpa.simple.SimpleCourseRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/*
 * Copyright 2020, Adam L. Davis
 */
@Controller
@RequestMapping("/web") //<-- every URL gets prefixed with this
public class CourseController {

    final SimpleCourseRepository simpleCourseRepository;
    final Counter getCounter;
    final Timer deleteTimer;

    public CourseController(final SimpleCourseRepository simpleCourseRepository, final MeterRegistry registry) {
        this.simpleCourseRepository = simpleCourseRepository;
        getCounter = registry.counter("requests", "get", "get_courses");
        deleteTimer = registry.timer("request_timer", "delete", "delete_course");
    }

    @GetMapping("/courses")
    public ModelAndView courses() {
        getCounter.increment();
        final ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("courses");
        modelAndView.addObject("courses", simpleCourseRepository.findAll());

        return modelAndView;
    }

    @DeleteMapping("/courses/{id}")
    public ModelAndView delete(@PathVariable final String id) {
        deleteTimer.record(() -> {
            System.out.println("Deleting Course with id=" + id);
            simpleCourseRepository.deleteById(Long.parseLong(id));
        });
        final ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("courses");
        modelAndView.addObject("courses", simpleCourseRepository.findAll());

        return modelAndView;
    }
}
