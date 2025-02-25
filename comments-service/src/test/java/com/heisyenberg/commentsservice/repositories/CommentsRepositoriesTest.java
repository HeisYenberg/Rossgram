package com.heisyenberg.commentsservice.repositories;

import com.heisyenberg.commentsservice.data.CommentsGenerator;
import com.heisyenberg.commentsservice.models.Comment;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@Import(CommentsGenerator.class)
public class CommentsRepositoriesTest {
  @Container
  private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
      new PostgreSQLContainer<>("postgres:15").withInitScript("schema.sql");

  @Autowired private CommentsRepository commentsRepository;
  @Autowired private CommentsGenerator commentsGenerator;
  private Comment testComment;

  @DynamicPropertySource
  private static void registerPostgresProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
  }

  @BeforeEach
  public void setup() {
    Comment generatedComment = commentsGenerator.generateComment();
    generatedComment.setId(null);
    testComment = commentsRepository.save(generatedComment);
  }

  @AfterEach
  public void teardown() {
    commentsRepository.delete(testComment);
  }

  @Test
  @DisplayName("Check save comment")
  public void checkSaveComment() {
    Comment comment = commentsGenerator.generateComment();
    comment.setId(null);
    Comment savedComment = commentsRepository.save(comment);
    comment.setId(savedComment.getId());
    Optional<Comment> foundComment = commentsRepository.findById(savedComment.getId());
    Assertions.assertTrue(foundComment.isPresent());
    Assertions.assertEquals(comment, foundComment.get());
    commentsRepository.delete(comment);
  }

  @Test
  @DisplayName("Check find comment by id")
  public void checkFindCommentById() {
    Optional<Comment> comment = commentsRepository.findById(testComment.getId());
    Assertions.assertTrue(comment.isPresent());
    Assertions.assertEquals(testComment, comment.get());
  }

  @Test
  @DisplayName("Check update comment")
  public void checkUpdateComment() {
    Comment comment = commentsGenerator.generateComment();
    comment.setId(testComment.getId());
    Comment updatedComment = commentsRepository.save(comment);
    Assertions.assertEquals(comment, updatedComment);
  }

  @Test
  @DisplayName("Check delete comment")
  public void checkDeleteComment() {
    commentsRepository.delete(testComment);
    Optional<Comment> comment = commentsRepository.findById(testComment.getId());
    Assertions.assertTrue(comment.isEmpty());
  }

  @Test
  @DisplayName("Check find all by image id and not deleted")
  public void checkFindAllByImageIdAndNotDeleted() {
    List<Comment> comments =
        commentsRepository.findAllByImageIdAndDeletedFalse(testComment.getImageId());
    Assertions.assertTrue(comments.contains(testComment));
  }

  @Test
  @DisplayName("Check find by id and image id and not deleted")
  public void checkFindByImageIdAndNotDeleted() {
    Optional<Comment> comment =
        commentsRepository.findByIdAndImageIdAndDeletedFalse(
            testComment.getId(), testComment.getImageId());
    Assertions.assertTrue(comment.isPresent());
    Assertions.assertEquals(testComment, comment.get());
  }
}
