/*
 * Copyright 2013-2018 the original author or authors.
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
package com.apress.spring_quick.jpa.custom;

import com.apress.spring_quick.jpa.simple.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Class with the implementation of the custom repository code. Uses JDBC in this case. For basic programmatic setup see
 * {@link CourseRepositoryImpl} for examples.
 */
@Profile("jdbc")
@Component("courseRepositoryImpl")
class CourseRepositoryImplJdbc extends JdbcDaoSupport implements CourseRepositoryCustom {

    private static final String COMPLICATED_SQL = "SELECT * FROM Course";

    @Autowired
    public CourseRepositoryImplJdbc(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public List<Course> myCustomBatchOperation() {
        return getJdbcTemplate().query(COMPLICATED_SQL, new CourseRowMapper());
    }

    private static class CourseRowMapper implements RowMapper<Course> {

        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {

            final Course course = new Course(rs.getLong("id"));

            course.setName(rs.getString("name"));
            course.setSubtitle(rs.getString("subtitle"));
            course.setDescription(rs.getString("description"));

            return course;
        }
    }
}
