package com.heisyenberg.commentsservice.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.heisyenberg.commentsservice.data.CommentsGenerator;
import com.heisyenberg.commentsservice.exceptions.CommentException;
import com.heisyenberg.commentsservice.models.Comment;
import com.heisyenberg.commentsservice.models.CommentDto;
import com.heisyenberg.commentsservice.repositories.CommentsRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(CommentsService.class)
@ExtendWith(MockitoExtension.class)
@Import(CommentsGenerator.class)
public class CommentsServiceTest {
  @Autowired private CommentsService commentsService;
  @MockitoBean private CommentsRepository commentsRepository;
  @Autowired private CommentsGenerator commentsGenerator;
  private Comment testComment;

  @BeforeEach
  public void setup() {
    testComment = commentsGenerator.generateComment();
  }

  @Test
  @DisplayName("Check adding comment to image")
  public void checkAddingCommentToImage() {
    CommentDto commentDto = new CommentDto(testComment.getText(), testComment.getUserId());
    when(commentsRepository.save(any(Comment.class))).thenReturn(testComment);
    Comment result = commentsService.addCommentToImage(testComment.getImageId(), commentDto);
    Assertions.assertEquals(testComment, result);
  }

  @Test
  @DisplayName("Check get image comments")
  public void checkGetImageComments() {
    List<Comment> comments = commentsGenerator.generateImageComments();
    String imageId = comments.get(0).getImageId();
    when(commentsRepository.findAllByImageIdAndDeletedFalse(anyString())).thenReturn(comments);
    List<Comment> result = commentsService.getImageComments(imageId);
    Assertions.assertEquals(comments, result);
  }

  @Test
  @DisplayName("Check successfully get image comment")
  public void checkSuccessfullyGetImageComment() {
    when(commentsRepository.findByIdAndImageIdAndDeletedFalse(anyLong(), anyString()))
        .thenReturn(Optional.of(testComment));
    Comment result = commentsService.getImageComment(testComment.getId(), testComment.getImageId());
    Assertions.assertEquals(testComment, result);
  }

  @Test
  @DisplayName("Check unsuccessfully get image comment")
  public void checkUnsuccessfullyGetImageComment() {
    when(commentsRepository.findByIdAndImageIdAndDeletedFalse(anyLong(), anyString()))
        .thenReturn(Optional.empty());
    Assertions.assertThrows(
        CommentException.class,
        () -> commentsService.getImageComment(testComment.getId(), testComment.getImageId()));
  }

  @Test
  @DisplayName("Check successfully delete image comment")
  public void checkSuccessfullyDeleteImageComment() {
    testComment.setDeleted(true);
    when(commentsRepository.findByIdAndImageIdAndDeletedFalse(anyLong(), anyString()))
        .thenReturn(Optional.of(testComment));
    Assertions.assertDoesNotThrow(
        () -> commentsService.deleteComment(testComment.getId(), testComment.getImageId()));
  }

  @Test
  @DisplayName("Check unsuccessfully delete image comment")
  public void checkUnsuccessfullyDeleteImageComment() {
    testComment.setDeleted(false);
    when(commentsRepository.findByIdAndImageIdAndDeletedFalse(anyLong(), anyString()))
        .thenReturn(Optional.empty());
    Assertions.assertThrows(
        CommentException.class,
        () -> commentsService.deleteComment(testComment.getId(), testComment.getImageId()));
  }
}
