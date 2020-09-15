DROP TABLE course IF EXISTS;

CREATE TABLE course  (
    course_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    title VARCHAR(200),
    description VARCHAR(250)
);
