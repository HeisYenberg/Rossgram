package com.heisyenberg.gatewayservice.services;


import com.heisyenberg.gatewayservice.dtos.Comment;
import com.heisyenberg.gatewayservice.dtos.CommentData;
import com.heisyenberg.gatewayservice.dtos.CommentDto;
import com.heisyenberg.gatewayservice.services.clients.CommentsServiceClient;
import com.heisyenberg.gatewayservice.services.clients.ImagesServiceClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentsService {
  private final CommentsServiceClient commentsServiceClient;
  private final ImagesServiceClient imagesServiceClient;
  private final JwtService jwtService;

  public ResponseEntity<?> createComment(
      final String authorization, final String imageId, final CommentData commentData) {
    String userId = jwtService.getUserIdFromAuthHeader(authorization);
    imagesServiceClient.getImage(Long.valueOf(imageId));
    CommentDto commentDto = new CommentDto(commentData.getCommentText(), userId);
    return commentsServiceClient.addComment(imageId, commentDto);
  }

  public ResponseEntity<List<Comment>> getComments(final String imageId) {
    return commentsServiceClient.getImageComments(imageId);
  }

  public ResponseEntity<String> deleteComment(
      final String authorization, final String imageId, final Long commentId) {
    String userId = jwtService.getUserIdFromAuthHeader(authorization);
    Comment comment = commentsServiceClient.getImageComment(imageId, commentId).getBody();
    if (comment.getUserId().equals(userId)) {
      return commentsServiceClient.deleteImageComment(imageId, commentId);
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }
}
