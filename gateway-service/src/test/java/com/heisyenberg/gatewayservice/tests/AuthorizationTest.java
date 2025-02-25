package com.heisyenberg.gatewayservice.tests;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.heisyenberg.gatewayservice.GatewayServiceApplication;
import com.heisyenberg.gatewayservice.data.UsersGenerator;
import com.heisyenberg.gatewayservice.dtos.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = GatewayServiceApplication.class)
@WireMockTest(httpPort = 8081)
@AutoConfigureMockMvc
@Import({UsersGenerator.class})
public class AuthorizationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UsersGenerator usersGenerator;

  @DynamicPropertySource
  private static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("users-service.url", () -> "http://localhost:8081");
  }

  @Test
  @DisplayName("Check authorization")
  public void testAuthorization() throws Exception {
    UserDto userDto = usersGenerator.createUserDto();
    stubFor(
        WireMock.post(urlEqualTo("/authorization"))
            .withRequestBody(equalToJson(objectMapper.writeValueAsString(userDto)))
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
    mockMvc
        .perform(
            post("/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Check registration")
  public void testRegistration() throws Exception {
    UserDto userDto = usersGenerator.createUserDto();
    stubFor(
        WireMock.post(urlEqualTo("/registration"))
            .withRequestBody(equalToJson(objectMapper.writeValueAsString(userDto)))
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())));
    mockMvc
        .perform(
            post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isOk());
  }
}
