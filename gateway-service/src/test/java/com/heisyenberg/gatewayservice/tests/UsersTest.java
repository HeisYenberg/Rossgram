package com.heisyenberg.gatewayservice.tests;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.heisyenberg.gatewayservice.GatewayServiceApplication;
import com.heisyenberg.gatewayservice.data.UsersGenerator;
import com.heisyenberg.gatewayservice.dtos.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = GatewayServiceApplication.class)
@WireMockTest(httpPort = 8081)
@AutoConfigureMockMvc
@Import({UsersGenerator.class})
public class UsersTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UsersGenerator usersGenerator;

  @DynamicPropertySource
  private static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("users-service.url", () -> "http://localhost:8081");
  }

  @Test
  @DisplayName("Check successfully get user by id")
  void testSuccessfullyGetUserById() throws Exception {
    User user = usersGenerator.createUser();
    stubFor(
        WireMock.get(urlEqualTo("/users/" + user.getId()))
            .willReturn(
                aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withBody(objectMapper.writeValueAsString(user))));
    mockMvc
        .perform(get("/users/{user_id}", user.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(user)));
  }

  @Test
  @DisplayName("Check unsuccessfully get user by id")
  void testUnsuccessfullyGetUserById() throws Exception {
    User user = usersGenerator.createUser();
    stubFor(
        WireMock.get(urlEqualTo("/users/" + user.getId()))
            .willReturn(aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    mockMvc
        .perform(get("/users/{user_id}", user.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isServiceUnavailable());
  }

  @Test
  @DisplayName("Check successfully get users")
  void testSuccessfullyGetUsers() throws Exception {
    String limit = "10";
    String skip = "0";
    List<User> users = usersGenerator.generateUsers();
    Page<User> usersPage = new PageImpl<>(users);
    stubFor(
        WireMock.get(urlPathEqualTo("/users"))
            .withQueryParam("limit", equalTo(limit))
            .withQueryParam("skip", equalTo(skip))
            .willReturn(
                aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withBody(objectMapper.writeValueAsString(usersPage))
                    .withHeader("Content-Type", "application/json")));
    String usersJson = objectMapper.writeValueAsString(users);
    mockMvc
        .perform(get("/users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value(objectMapper.readValue(usersJson, List.class)));
  }

  @Test
  @DisplayName("Check unsuccessfully get users")
  void testUnsuccessfullyGetUsers() throws Exception {
    String limit = "10";
    String skip = "0";
    stubFor(
        WireMock.get(urlEqualTo("/users"))
            .withQueryParam("limit", equalTo(limit))
            .withQueryParam("skip", equalTo(skip))
            .willReturn(aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    mockMvc
        .perform(get("/users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isServiceUnavailable());
  }
}
