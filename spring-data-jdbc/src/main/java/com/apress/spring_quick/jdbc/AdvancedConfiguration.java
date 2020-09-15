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

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;

@Configuration
@EnableJdbcRepositories
public class AdvancedConfiguration extends JdbcConfiguration {

    final AtomicInteger id = new AtomicInteger(0);

    @Bean
    public ApplicationListener<?> idSetting() {

        return (ApplicationListener<BeforeSaveEvent>) event -> {

            if (event.getEntity() instanceof Channel) {
                setIds((Channel) event.getEntity());
            }
        };
    }

    private void setIds(Channel channel) {

        if (channel.getId() == 0) {
            channel.setId(id.incrementAndGet());
        }

        Message message = channel.getMessage();

        if (message != null) {
            message.setId((long) channel.getId());
        }
    }

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(asList(new Converter<Clob, String>() {
            @Nullable
            @Override
            public String convert(final Clob clob) {
                try {
                    return Math.toIntExact(clob.length()) == 0
                            ? ""
                            : clob.getSubString(1, Math.toIntExact(clob.length()));

                } catch (SQLException e) {
                    throw new IllegalStateException("Failed to convert CLOB to String.", e);
                }
            }
        }));
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcOperations operations) {
        return new NamedParameterJdbcTemplate(operations);
    }
}
