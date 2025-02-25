package com.heisyenberg.commentsservice.services;

import com.heisyenberg.commentsservice.exceptions.CommentException;
import com.heisyenberg.commentsservice.models.Comment;
import com.heisyenberg.commentsservice.models.CommentDto;
import com.heisyenberg.commentsservice.repositories.CommentsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentsService {
  private final CommentsRepository commentsRepository;

  public Comment addCommentToImage(final String imageId, final CommentDto commentDto) {
    final Comment comment =
        Comment.builder()
            .imageId(imageId)
            .userId(commentDto.getUserId())
            .text(commentDto.getCommentText())
            .deleted(false)
            .build();
    return commentsRepository.save(comment);
  }

  public List<Comment> getImageComments(final String imageId) {
    return commentsRepository.findAllByImageIdAndDeletedFalse(imageId);
  }

  public Comment getImageComment(final Long commentId, final String imageId) {
    return commentsRepository
        .findByIdAndImageIdAndDeletedFalse(commentId, imageId)
        .orElseThrow(() -> new CommentException("Comment not exists or was deleted"));
  }

  public void deleteComment(final Long commentId, final String imageId) {
    Comment comment =
        commentsRepository
            .findByIdAndImageIdAndDeletedFalse(commentId, imageId)
            .orElseThrow(() -> new CommentException("Comment not exists or was deleted"));
    comment.setDeleted(true);
    commentsRepository.save(comment);
  }
}
