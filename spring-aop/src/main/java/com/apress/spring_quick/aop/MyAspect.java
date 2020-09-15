package com.apress.spring_quick.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/*
 * Copyright 2020, Adam L. Davis
 */

/**
 * Spring AOP Aspect demonstrating using Spring AOP with Pointcuts.
 */
@Component
@Aspect
public class MyAspect {

    @Pointcut("execution(* com.apress.spring_quick.aop.Repository.*(..))")
    public void repository() {
    }

    // alternative: use the annotation on the class to advise every method on every Repository:
    @Pointcut("@target(org.springframework.stereotype.Repository)")
    public void allRepositories() {}

    // alternative 2: use an annotation on each actual method to advise:
    @Pointcut("@annotation(com.apress.spring_quick.aop.LogMe)")
    public void logMes() {}

    // uses @Around to time each method call and print out how long they take
    @Around("repository()")
    public Object timeMethodCall(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch(proceedingJoinPoint.toShortString());
        final Object result;
        try {
            stopWatch.start(proceedingJoinPoint.toLongString());
            result = proceedingJoinPoint.proceed();
        } finally {
            stopWatch.stop();
        }
        System.out.println(stopWatch.toString());
        return result;
    }

    // Print out a statement before each method call. (Should use Logger in real applications)
    @Before("logMes()")
    public void beforeLog(final JoinPoint joinPoint) {
        System.out.println("INFO: Calling " + joinPoint.toLongString());
    }

    // Pointcut expressions can be combined using &&, || and ! operators:
    @AfterReturning("repository() && logMes()")
    public void afterCall(final JoinPoint joinPoint) {
        System.out.println("INFO: Call was successful! " + joinPoint.toLongString());
    }

}
