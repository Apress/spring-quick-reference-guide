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

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * A repository for {@link Channel}.
 */
interface ChannelRepository extends CrudRepository<Channel, Integer> {

	// Spring JDBC automatically maps from selected rows to a custom Object (ChannelReport)
	@Query("SELECT t.name topic_name, t.description, c.name channel_name" +
			"  FROM topic t" +
			"  JOIN channel c" +
			"  ON t.channel = c.id" +
			"  WHERE :age BETWEEN c.min_age and c.max_age")
	List<ChannelReport> channelReportForAge(@Param("age") int age);

	// @Modifying marks a query that modifies the database. This one changes all topic names to lowercase.
	@Modifying
	@Query("UPDATE topic set name = lower(name) WHERE name <> lower(name)")
	int lowerCaseTopicNames();
	// returns the number of changed rows
}
