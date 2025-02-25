package com.heisyenberg.commentsservice.repositories;

import com.heisyenberg.commentsservice.models.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByImageIdAndDeletedFalse(final String imageId);

  Optional<Comment> findByIdAndImageIdAndDeletedFalse(final Long id, final String imageId);
}
