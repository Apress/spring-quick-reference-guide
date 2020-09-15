package com.apress.spring_quick.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Copyright 2020, Adam L. Davis
 */
public class ReactorTests {

    @Test
    public void testStepVerifier_Mono_error() {
        Mono<String> monoError = Mono.error(new RuntimeException("error")); //1
        StepVerifier.create(monoError) //2
                .expectErrorMessage("error") //3
                .verify(); //4
    }
    
    @Test
    public void testStepVerifier_Mono_foo() {
        Mono<String> foo = Mono.just("foo"); //1
        StepVerifier.create(foo)             //2
                .expectNext("foo")                 //3
                .verifyComplete();                 //4
    }

    @Test
    public void testStepVerifier_Flux() {
        Flux<Integer> flux = Flux.just(1, 4, 9); //1
        StepVerifier.create(flux)                //2
                .expectNext(1)                         //3
                .expectNext(4)
                .expectNext(9)
                .expectComplete()                      //4
                .verify(Duration.ofSeconds(10));       //5
    }

    @Test
    public void testPublisher() {
        TestPublisher<Object> publisher = TestPublisher.create(); //1
        Flux<Object> stringFlux = publisher.flux();               //2
        List list = new ArrayList();                              //3
        stringFlux.subscribe(next -> list.add(next),
                ex -> ex.printStackTrace());         //4
        publisher.emit("foo", "bar");                             //5
        assertEquals(2, list.size());                             //6
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
    }
}
