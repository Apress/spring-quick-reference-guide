package com.apress.spring_quick.di;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
 * Copyright 2020, Adam L. Davis
 */

/**
 * A simple Bean with two injected values.
 */
@Component
public class MyBean implements MyBeanInterface {

    @Value("${comment.prefix}")
    String commentPrefix;

    @Value("${comment.suffix}")
    String commentSuffix;

    public String getComment() {
        return commentPrefix + "Hello" + commentSuffix;
    }

}
