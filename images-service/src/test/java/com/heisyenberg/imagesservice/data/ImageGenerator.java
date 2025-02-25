package com.heisyenberg.imagesservice.data;

import com.github.javafaker.Faker;
import com.heisyenberg.imagesservice.models.Image;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class ImageGenerator {
  private final Faker faker = new Faker();

  public Image generateImage() {
    return Image.builder()
        .id(faker.number().randomNumber())
        .userId(faker.number().digit())
        .imageBase64(faker.harryPotter().character())
        .deleted(false)
        .build();
  }
}
