package com.apress.spring_quick.aop;

/*
 * Copyright 2020, Adam L. Davis
 */

/**
 * A pretend Repository just to demonstrate AOP features.
 */
public interface Repository {

    Object save(Object o);

    Iterable<Object> findAll();

    void delete(Object o);

}
