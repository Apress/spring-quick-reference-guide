package com.apress.spring_quick.aop;

import java.util.List;

/*
 * Copyright 2020, Adam L. Davis
 */
@org.springframework.stereotype.Repository
public class FakeRepository implements Repository {

    @Override
    public Object save(Object o) {
        wasteTime((int) (Math.random() * 1000));
        return o;
    }

    @Override
    public Iterable<Object> findAll() {
        wasteTime((int) (Math.random() * 1000));
        return List.of(1, 2, 3);
    }

    @LogMe //<-- See MyAspect.logMes
    @Override
    public void delete(Object o) {
        wasteTime((int) (Math.random() * 1000));
    }

    private static void wasteTime(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
