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

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.time.LocalDateTime;

/**
 * Larger groups of {@link Channel}s, like "Programming" or "Jobs".
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@PersistenceConstructor))
public class Category {

    private final @Id
    @With
    Long id;

    private String name, description;

    @CreatedDate // this gets set automatically by Spring Data
    private LocalDateTime created;

    private @Setter
    long inserted;

    private AgeGroup ageGroup;

    public Category(String name, String description, AgeGroup ageGroup) {

        this.id = null;
        this.name = name;
        this.description = description;
        this.ageGroup = ageGroup;
        this.created = LocalDateTime.now();
    }

    /** Called by BeforeSaveCallback - see {@link CategoryConfiguration}. */
    public void timeStamp() {

        if (inserted == 0) {
            inserted = System.currentTimeMillis();
        }
    }
}
