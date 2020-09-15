/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apress.spring_quick.jdbc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * A Channel for discussions.
 */
@Data
@AccessType(Type.PROPERTY)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Channel {

    private @Id
    int id;
    private String name;
    private @Transient
    Period minimumAge, maximumAge;

    @Column("message_id")
    private Message message;

    @Column(keyColumn = "name")
    private final @AccessType(Type.FIELD)
    @With(AccessLevel.PACKAGE)
    Map<String, Topic> topics;

    Channel() {
        this.topics = new HashMap<>();
    }

    // conversion for custom types currently has to be done through getters/setter + marking the underlying property with
    // @Transient.
    @Column("min_age")
    public int getIntMinimumAge() {
        return toInt(this.minimumAge);
    }

    public void setIntMinimumAge(int years) {
        minimumAge = toPeriod(years);
    }

    @Column("max_age")
    public int getIntMaximumAge() {
        return toInt(this.maximumAge);
    }

    public void setIntMaximumAge(int years) {
        maximumAge = toPeriod(years);
    }

    private static int toInt(Period period) {
        return (int) (period == null ? 0 : period.get(ChronoUnit.YEARS));
    }

    private static Period toPeriod(int years) {
        return Period.ofYears(years);
    }

    public void addTopic(String name, String description) {

        Topic topic = new Topic(name, description);
        topics.put(name, topic);
    }
}
