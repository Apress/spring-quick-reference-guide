package com.apress.springquick.springbootmvc;

import com.apress.spring_quick.jpa.simple.SimpleCourseRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

// by default it would scan the current package and everything under it
// scanning "com.apress.spring_quick.jpa.simple" initializes the SimpleCourseRepository
@SpringBootApplication(scanBasePackages = "com.apress.spring_quick.jpa.simple")
@EnableWebSecurity
public class SpringBootMvcApplication extends WebSecurityConfigurerAdapter {

    // from WebSecurityConfigurerAdapter
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // here you could configure a JDBC database
        // auth.jdbcAuthentication().usersByUsernameQuery(...)
        auth.inMemoryAuthentication()
                .withUser(User.builder().username("admin").password("123")
                        .roles("USER", "ADMIN")
                        .build());
    }

    // from WebSecurityConfigurerAdapter
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeRequests()
                .antMatchers("/api/v1/**").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and().csrf().disable();
    }

    // Beans can also be defined right here since @SpringBootApplication includes @SpringBootConfiguration
    @Bean
    public CourseController courseController(
            final SimpleCourseRepository courseRepository,
            final MeterRegistry meterRegistry) {
        // MeterRegistry is automatically available due to including the spring-actuator-starter
        return new CourseController(courseRepository, meterRegistry);
    }

    @Bean
    public CourseRestController courseRestController(final SimpleCourseRepository courseRepository) {
        return new CourseRestController(courseRepository);
    }

    @Bean
    public CustomHealthIndicator customHealthIndicator() {
        return new CustomHealthIndicator();
    }

    @Bean
    public CustomInfoContributor customInfoContributor(final SimpleCourseRepository courseRepository) {
        return new CustomInfoContributor(courseRepository);
    }

    // this main method starts up Spring
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMvcApplication.class, args);
    }

}
