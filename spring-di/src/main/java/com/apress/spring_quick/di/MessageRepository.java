package com.apress.spring_quick.di;

import com.apress.spring_quick.di.model.Message;

/*
 * Copyright 2020, Adam L. Davis
 */
public interface MessageRepository {

    Iterable<Message> findAll();

    Message save(Message m);

    void delete(Message m);

}
