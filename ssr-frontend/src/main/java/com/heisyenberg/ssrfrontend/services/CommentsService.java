package com.heisyenberg.ssrfrontend.services;

import com.heisyenberg.ssrfrontend.dtos.Comment;
import com.heisyenberg.ssrfrontend.dtos.CommentData;
import com.heisyenberg.ssrfrontend.services.clients.CommentsServiceClient;
import com.heisyenberg.ssrfrontend.services.clients.UsersServiceClient;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentsService {
  private static final String BEARER_PREFIX = "Bearer ";
  private final CommentsServiceClient commentsServiceClient;
  private final UsersServiceClient usersServiceClient;

  public void addCommentToImage(final String imageId, final CommentData commentData) {
    String token = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
    commentsServiceClient.createComment(BEARER_PREFIX + token, imageId, commentData);
  }

  public void deleteCommentFromImage(final String imageId, final Long commentId) {
    String token = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
    commentsServiceClient.deleteComment(BEARER_PREFIX + token, imageId, commentId);
  }

  public List<Comment> getComments(final Long imageId) {
    return commentsServiceClient.getComments(imageId.toString()).getBody().stream()
        .peek(c -> c.setUsername(usersServiceClient.getUserById(c.getUserId()).getBody()))
        .sorted(Comparator.comparing(Comment::getId))
        .toList();
  }
}
