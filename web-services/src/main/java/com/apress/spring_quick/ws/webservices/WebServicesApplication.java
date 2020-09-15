package com.apress.spring_quick.ws.webservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;

import java.util.List;

@EnableWs
@SpringBootApplication
public class WebServicesApplication extends WsConfigurerAdapter {

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        super.addInterceptors(interceptors);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebServicesApplication.class, args);
    }

}
