package com.apress.spring_quick.mobile;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/*
 * Copyright 2020, Adam L. Davis
 */
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/**").permitAll()
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
}
