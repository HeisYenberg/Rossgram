package com.heisyenberg.commentsservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heisyenberg.commentsservice.data.CommentsGenerator;
import com.heisyenberg.commentsservice.exceptions.CommentException;
import com.heisyenberg.commentsservice.models.Comment;
import com.heisyenberg.commentsservice.models.CommentDto;
import com.heisyenberg.commentsservice.services.CommentsService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentsController.class)
@ExtendWith(MockitoExtension.class)
@Import(CommentsGenerator.class)
public class CommentsControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockitoBean private CommentsService commentsService;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private CommentsGenerator commentsGenerator;
  private Comment testComment;

  @BeforeEach
  public void setup() {
    testComment = commentsGenerator.generateComment();
  }

  @Test
  @DisplayName("Check adding comment")
  void checkAddingComment() throws Exception {
    CommentDto commentDto = new CommentDto(testComment.getText(), testComment.getUserId());
    when(commentsService.addCommentToImage(anyString(), any(CommentDto.class)))
        .thenReturn(testComment);
    mockMvc
        .perform(
            post("/images/{image_id}/comments", testComment.getImageId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
        .andExpect(status().isCreated())
        .andExpect(content().string(objectMapper.writeValueAsString(testComment)));
  }

  @Test
  @DisplayName("Check get image comments")
  void checkGetImageComments() throws Exception {
    List<Comment> comments = commentsGenerator.generateImageComments();
    String imageId = comments.get(0).getImageId();
    when(commentsService.getImageComments(anyString())).thenReturn(comments);
    mockMvc
        .perform(
            get("/images/{image_id}/comments", imageId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(comments)));
  }

  @Test
  @DisplayName("Check successfully get image comment")
  void checkSuccessfullyGetImageComments() throws Exception {
    when(commentsService.getImageComment(anyLong(), anyString())).thenReturn(testComment);
    mockMvc
        .perform(
            get(
                "/images/{image_id}/comments/{comment_id}",
                testComment.getId(),
                testComment.getImageId()))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(testComment)));
  }

  @Test
  @DisplayName("Check unsuccessfully get image comment")
  void checkUnsuccessfullyGetImageComments() throws Exception {
    when(commentsService.getImageComment(anyLong(), anyString())).thenThrow(CommentException.class);
    mockMvc
        .perform(
            get(
                "/images/{image_id}/comments/{comment_id}",
                testComment.getId(),
                testComment.getImageId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Check successfully delete image comment")
  void checkSuccessfullyDeleteImageComments() throws Exception {
    doNothing().when(commentsService).deleteComment(anyLong(), anyString());
    mockMvc
        .perform(
            delete(
                "/images/{image_id}/comments/{comment_id}",
                testComment.getId(),
                testComment.getImageId()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Check unsuccessfully delete image comment")
  void checkUnsuccessfullyDeleteImageComments() throws Exception {
    doThrow(CommentException.class).when(commentsService).deleteComment(anyLong(), anyString());
    mockMvc
        .perform(
            delete(
                "/images/{image_id}/comments/{comment_id}",
                testComment.getId(),
                testComment.getImageId()))
        .andExpect(status().isNotFound());
  }
}
