package com.heisyenberg.ssrfrontend.services;

import static java.lang.String.format;

import com.heisyenberg.ssrfrontend.dtos.Image;
import com.heisyenberg.ssrfrontend.dtos.ImageData;
import com.heisyenberg.ssrfrontend.dtos.ImageId;
import com.heisyenberg.ssrfrontend.services.clients.ImagesServiceClient;
import com.heisyenberg.ssrfrontend.services.clients.UsersServiceClient;
import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectionsService {
  private static final String BEARER_PREFIX = "Bearer ";
  private final UsersServiceClient usersServiceClient;
  private final ImagesServiceClient imagesServiceClient;

  public void addImageToCollection(final MultipartFile imageFile) {
    String token = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
    String type = imageFile.getContentType();
    try {
      String encoded = Base64.getEncoder().encodeToString(imageFile.getBytes());
      ImageData image = new ImageData(format("data:image/%s;base64,%s", type, encoded));
      usersServiceClient.addImageToCollection(BEARER_PREFIX + token, image);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteImageById(final String imageId) {
    String token = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
    usersServiceClient.deleteImageFromCollection(BEARER_PREFIX + token, new ImageId(imageId));
  }

  public String getUserById(final String userId) {
    return usersServiceClient.getUserById(userId).getBody();
  }

  public List<Image> getUserCollection(final String userId) {
    Set<String> collection = usersServiceClient.getUsersCollection(userId).getBody();
    return collection.stream()
        .map(
            i ->
                Image.builder()
                    .id(Long.valueOf(i))
                    .imageBase64(imagesServiceClient.getImage(Long.valueOf(i)).getBody())
                    .build())
        .sorted(Comparator.comparing(Image::getId).reversed())
        .toList();
  }
}
