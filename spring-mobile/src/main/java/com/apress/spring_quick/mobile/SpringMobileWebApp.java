package com.apress.spring_quick.mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.io.IOException;

@SpringBootApplication
@Import({WebConfig.class, ServiceConfig.class})
public class SpringMobileWebApp {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringMobileWebApp.class, args);
    }
}
