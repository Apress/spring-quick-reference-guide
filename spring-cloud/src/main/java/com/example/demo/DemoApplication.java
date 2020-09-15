package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

@EnableEurekaClient
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public Supplier<String> source1() {
		return () -> "" + new Date();
	}

	@Bean
	public Supplier<String> source2() {
		return () -> "HI!";
	}
	@Bean
	public Consumer<String> sink() {
		return System.out::println;
	}
}
