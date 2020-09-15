package com.apress.spring_quick.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.retry.Jitter;
import reactor.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Copyright 2020, Adam L. Davis
 */
public class ReactorExamples {

    public void parallel() {
        Flux.range(1, 100)
                .publishOn(Schedulers.parallel())
                .subscribe(v -> doSomething(v));
    }

    public void doSomething(Integer i) {
        System.out.println("something: " + i);
    }

    public void examples() {
        Flux<String> flux1 = Flux.just("a", "b", "foobar");       //1
        List<String> iterable = Arrays.asList("a", "b", "foobar");
        Flux<String> flux2 = Flux.fromIterable(iterable);         //2
        Flux<Integer> numbers = Flux.range(1, 64);                //3
        Mono<String> noData = Mono.empty();   //1
        Mono<String> data = Mono.just("foo"); //2
        Mono<String> monoError = Mono.error(new RuntimeException("error")); //3
        Flux<Long> squares = Flux.generate(
                AtomicLong::new, //1
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next(i * i); //2
                    if (i == 10) sink.complete(); //2
                    return state;
                });
    }

    /** Using Schedulers example. */
    public List<Integer> squares() {
        final List<Integer> squares = new ArrayList<>();
        Flux.range(1, 64).flatMap(v -> // 1
                Mono.just(v)
                        .subscribeOn(Schedulers.parallel()) // was .newSingle("comp"))
                        .map(w -> w * w))                                    //2
                .doOnError(ex -> ex.printStackTrace())               //3
                .doOnComplete(() -> System.out.println("Completed")) //4
                .publishOn(Schedulers.immediate())
                .subscribe(squares::add);                            //5

        return squares;
    }

    /** Example of using zipWhen. */
    public Mono<Integer> getStudentCount(final Long id) {
        return getCourse(id)
                .zipWhen(course -> getStudentCount(course))
                .map(tuple2 -> tuple2.getT2());
    }

    Mono<Integer> getStudentCount(final Course course) {
        return Mono.just(123); // call to database
    }

    Mono<Course> getCourse(final Long id) {
        return Mono.just(new Course(id)); // call to database
    }

    public static class Course {
        Long id;
        String name;
        public Course(Long id) {
            this.id = id;
            this.name = "My Course";
        }
    }
    
    /** Jitter, Retry, Backoff 
     * @return Flux of courses from given flux but now with retry logic. 
     */
    public Flux<Course> retry(Object appContext, Flux<Course> flux) {
        Retry retry = Retry.anyOf(IOException.class)           //1
                .exponentialBackoff(Duration.ofMillis(100),  //2
                        Duration.ofSeconds(60))
                .jitter(Jitter.random())                     //3
                .retryMax(5)
                .withApplicationContext(appContext);          //4
                //.doOnRetry(context ->
                  //      context.applicationContext().rollback());
        
        return flux.retryWhen(retry);                         //5
    }

}
