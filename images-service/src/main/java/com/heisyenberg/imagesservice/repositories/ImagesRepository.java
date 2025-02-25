package com.heisyenberg.imagesservice.repositories;

import com.heisyenberg.imagesservice.models.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {
  Optional<Image> findByIdAndDeletedFalse(final Long id);
}
