package com.apress.springquick.springbootmvc;

import com.apress.spring_quick.jpa.simple.Course;
import com.apress.spring_quick.jpa.simple.SimpleCourseRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.print.attribute.standard.Severity;
import javax.servlet.ServletContext;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/*
 * Copyright 2020, Adam L. Davis
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class ControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void coursesShouldReturnAllCourses() throws Exception {
        Course course = new Course();
        course.setName("Java Professional");
        course.setSubtitle("Java 11");
        course.setDescription("");
        //when(courseRepository.findAll()).thenReturn(List.of(course));
        mockMvc.perform(get("/api/v1/courses")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("[{\"id\":null,\"title\":\"Java Professional\"" +
                                ",\"subtitle\":\"Java 11\",\"description\":\"\"}]")));
    }

}
