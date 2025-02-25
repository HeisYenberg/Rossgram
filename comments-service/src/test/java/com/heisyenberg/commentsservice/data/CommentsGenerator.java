package com.heisyenberg.commentsservice.data;

import com.github.javafaker.Faker;
import com.heisyenberg.commentsservice.models.Comment;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class CommentsGenerator {
  private static final int LIST_SIZE = 10;
  private final Faker faker = new Faker();

  public Comment generateComment() {
    return Comment.builder()
        .id(faker.number().randomNumber())
        .imageId(faker.number().digit())
        .userId(faker.number().digit())
        .text(faker.harryPotter().spell())
        .deleted(false)
        .build();
  }

  public List<Comment> generateImageComments() {
    String imageId = faker.number().digit();
    List<Comment> comments = new ArrayList<>(LIST_SIZE);
    for (int i = 0; i < LIST_SIZE; i++) {
      comments.add(
          Comment.builder()
              .id(faker.number().randomNumber())
              .imageId(imageId)
              .userId(faker.number().digit())
              .text(faker.harryPotter().spell())
              .deleted(false)
              .build());
    }
    return comments;
  }
}
