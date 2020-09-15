package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.retry.support.RetryTemplate;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
  public FlatFileItemReader<Course> reader() {
    return new FlatFileItemReaderBuilder<Course>()
        .name("personItemReader")
        .resource(new ClassPathResource("sample-data.csv"))
        .delimited()
        .names(new String[]{"title", "description"})
        .fieldSetMapper(new BeanWrapperFieldSetMapper<Course>() {{
          setTargetType(Course.class);
        }})
        .build();
  }

  @Bean
  public CourseProcessor processor() {
    return new CourseProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<Course> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Course>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql("INSERT INTO course (title, description) VALUES (:title, :description)")
        .dataSource(dataSource)
        .build();
  }

  @Bean
  public Step readAndSaveStep(JdbcBatchItemWriter<Course> writer,
                              CourseProcessor processor) {
    return stepBuilderFactory.get("saveStep")
        .<Course, Course>chunk(100)
        .reader(reader())
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Job importCourseJob(JobCompletionListener listener, Step step) {
    return jobBuilderFactory.get("importCourseJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(step)
        .end()
        .build();
  }

  @Bean
  public ItemProcessor retryTemplateExample() {
    final CourseProcessor courseProcessor = processor();
    return new ItemProcessor<Course, Course>() {
      @Override
      public Course process(Course input) throws Exception {
        return RetryTemplate.builder()
                .maxAttempts(2)
                .exponentialBackoff(10, 2, 1000)
                .build()
                .execute((retryContext) -> courseProcessor.process(input));
      }
    };
  }
}
