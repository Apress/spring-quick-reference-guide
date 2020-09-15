package com.apress.springquick.springbootmvc;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.math.BigDecimal;

/*
 * Copyright 2020, Adam L. Davis
 */
public class CustomHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        // only one of the following should be used:
//        builder.status(Status.UP).withDetail("key", 123);
//        builder.status(Status.DOWN).withDetail("key", "value");
//        builder.status(Status.UNKNOWN).withDetail("key", "value");
        builder.status(Status.OUT_OF_SERVICE).withDetail("key", new BigDecimal("1.23"));
    }
}
