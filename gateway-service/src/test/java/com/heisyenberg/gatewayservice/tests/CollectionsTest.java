package com.heisyenberg.gatewayservice.tests;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.heisyenberg.gatewayservice.GatewayServiceApplication;
import com.heisyenberg.gatewayservice.data.ImagesGenerator;
import com.heisyenberg.gatewayservice.data.UsersGenerator;
import com.heisyenberg.gatewayservice.dtos.ImageData;
import com.heisyenberg.gatewayservice.services.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = GatewayServiceApplication.class)
@WireMockTest
@AutoConfigureMockMvc
@Import({UsersGenerator.class, ImagesGenerator.class})
public class CollectionsTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UsersGenerator usersGenerator;
  @Autowired private ImagesGenerator imagesGenerator;
  @MockitoBean JwtService jwtService;

  @DynamicPropertySource
  private static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("users-service.url", () -> "http://localhost:8081");
    registry.add("images-service.url", () -> "http://localhost:8083");
  }

  @Test
  @DisplayName("Check adding image")
  public void testAddImage() throws Exception {
    String userId = "1";
    ImageData imageData = imagesGenerator.generateImageData();
    when(jwtService.getUserIdFromAuthHeader(anyString())).thenReturn(userId);
  }
}
