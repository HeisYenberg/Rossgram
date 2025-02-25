package com.heisyenberg.gatewayservice.data;

import com.github.javafaker.Faker;
import com.heisyenberg.gatewayservice.dtos.ImageData;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class ImagesGenerator {
  private final Faker faker = new Faker();

  public ImageData generateImageData() {
    return new ImageData(faker.internet().image());
  }
}
