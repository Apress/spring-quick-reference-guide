package com.apress.spring_quick.mobile;

import com.apress.spring_quick.jpa.simple.SimpleCourseRepository;
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
@RequestMapping("/mobile")
public class MobileCourseController {

    final SimpleCourseRepository simpleCourseRepository;

    public MobileCourseController(final SimpleCourseRepository simpleCourseRepository) {
        this.simpleCourseRepository = simpleCourseRepository;
    }

    @GetMapping("/")
    public String home() {
        return "mobile/home";
    }

    @GetMapping("/courses")
    public ModelAndView courses() {
        final ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("mobile/courses");
        modelAndView.addObject("courses", simpleCourseRepository.findAll());

        return modelAndView;
    }

    @DeleteMapping("/courses/{id}")
    public ModelAndView delete(@PathVariable final String id) {
        final ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("mobile/courses");
        modelAndView.addObject("courses", simpleCourseRepository.findAll());

        return modelAndView;
    }
}
