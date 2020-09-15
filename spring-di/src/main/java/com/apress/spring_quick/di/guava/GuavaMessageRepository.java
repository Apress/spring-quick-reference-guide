package com.apress.spring_quick.di.guava;

/*
 * Copyright 2020, Adam L. Davis
 */

import com.apress.spring_quick.di.MessageRepository;
import com.apress.spring_quick.di.model.Message;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Implements an in-memory repository of Message using a Guava cache.
 */
@Repository // <-- ensures component-scan picks up this class
@Profile("guava") // <-- Marks this bean as only valid for this Profile (set using `spring.profiles.active`)

public class GuavaMessageRepository implements MessageRepository {

    final Cache<Long, Message> messageCache;
    final AtomicLong counter = new AtomicLong(1);

    public GuavaMessageRepository(
            @Value("${cache.ttl.hours}") final int cacheTtlHours,
            @Value("${cache.initial.size}") final int size) {
        messageCache = CacheBuilder.newBuilder()
                .concurrencyLevel(1)
                .initialCapacity(size)
                .expireAfterWrite(cacheTtlHours, TimeUnit.HOURS)
                .build();
    }

    @Override
    public Iterable<Message> findAll() {
        return messageCache.asMap().values();
    }

    @Override
    public Message save(Message m) {
        m.setId(counter.getAndIncrement());
        messageCache.put(m.getId(), m);
        return m;
    }

    @Override
    public void delete(Message m) {
        messageCache.invalidate(m.getId());
    }
}
