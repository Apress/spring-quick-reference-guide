package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class CourseProcessor implements ItemProcessor<Course, Course> {

  private static final Logger log = LoggerFactory.getLogger(CourseProcessor.class);

  @Retryable(maxAttempts = 4, backoff = @Backoff(random = true, delay = 100))
  @Override
  public Course process(final Course course) throws Exception {
    final String title = course.getTitle().replaceAll("\\s+", " ").trim();
    final String description = course.getDescription().replaceAll("\\s+", " ").trim();
    final Course transformedCourse = new Course(title, description);

    log.info("Converting (" + course + ") into (" + transformedCourse + ")");

    return transformedCourse;
  }
}
