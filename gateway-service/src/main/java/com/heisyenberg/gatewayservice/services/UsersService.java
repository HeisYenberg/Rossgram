package com.heisyenberg.gatewayservice.services;

import com.heisyenberg.gatewayservice.dtos.Image;
import com.heisyenberg.gatewayservice.dtos.ImageData;
import com.heisyenberg.gatewayservice.dtos.ImageDto;
import com.heisyenberg.gatewayservice.dtos.ImageId;
import com.heisyenberg.gatewayservice.dtos.User;
import com.heisyenberg.gatewayservice.services.clients.ImagesServiceClient;
import com.heisyenberg.gatewayservice.services.clients.UsersServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersService {
  private final UsersServiceClient usersServiceClient;
  private final ImagesServiceClient imagesServiceClient;
  private final JwtService jwtService;

  public ResponseEntity<?> addImageToCollection(
      final String authorization, final ImageData imageData) {
    String userId = jwtService.getUserIdFromAuthHeader(authorization);
    ImageDto imageDto = new ImageDto(imageData.getImageBase64(), userId);
    Image createdImage = imagesServiceClient.addImage(imageDto).getBody();
    String imageId = createdImage.getId().toString();
    try {
      return usersServiceClient.addImageToCollection(userId, new ImageId(imageId));
    } catch (FeignException e) {
      imagesServiceClient.deleteImage(Long.parseLong(imageId));
      throw e;
    }
  }

  public ResponseEntity<?> deleteImageFromCollection(
      final String authorization, final ImageId imageId) {
    String userId = jwtService.getUserIdFromAuthHeader(authorization);
    usersServiceClient.deleteImageFromCollection(userId, imageId);
    try {
      return imagesServiceClient.deleteImage(Long.parseLong(imageId.getImageId()));
    } catch (Exception e) {
      usersServiceClient.addImageToCollection(userId, imageId);
      throw e;
    }
  }

  public ResponseEntity<?> getUsersCollection(final String userId) {
    return usersServiceClient.getUserCollections(userId);
  }

  public ResponseEntity<String> getUserById(final String userId) {
    return usersServiceClient.getUserById(userId);
  }

  public ResponseEntity<Page<User>> getUsers(final int limit, final int skip) {
    return usersServiceClient.getUsers(limit, skip);
  }
}
