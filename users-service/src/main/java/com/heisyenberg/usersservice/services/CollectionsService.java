package com.heisyenberg.usersservice.services;

import com.heisyenberg.usersservice.exceptions.CollectionImageException;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.models.ImageDto;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserCollection;
import com.heisyenberg.usersservice.repositories.CollectionsRepository;
import com.heisyenberg.usersservice.repositories.UsersRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectionsService {
  private final UsersRepository usersRepository;
  private final CollectionsRepository collectionsRepository;

  public Set<String> getUserCollection(final Long userId) {
    return usersRepository
        .findById(userId)
        .orElseThrow(() -> new UserException("User not found"))
        .getUserCollection()
        .getImagesIds();
  }

  public void addImageToCollection(final Long userId, final ImageDto imageDto) {
    User user =
        usersRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
    UserCollection collection = user.getUserCollection();
    if (collection.getImagesIds().contains(imageDto.getImageId())) {
      throw new CollectionImageException("Image already exists");
    }
    collection.getImagesIds().add(imageDto.getImageId());
    collectionsRepository.save(collection);
  }

  public void removeImageFromCollection(final Long userId, final ImageDto imageDto) {
    User user =
        usersRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
    UserCollection collection = user.getUserCollection();
    if (!collection.getImagesIds().contains(imageDto.getImageId())) {
      throw new CollectionImageException("Image does not exist");
    }
    collection.getImagesIds().remove(imageDto.getImageId());
    collectionsRepository.save(collection);
  }
}
