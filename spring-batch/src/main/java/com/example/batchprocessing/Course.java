package com.example.batchprocessing;

public class Course {

  private String title;
  private String description;

  public Course() {
  }

  public Course(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "title: " + title + ", description: " + description;
  }

}
