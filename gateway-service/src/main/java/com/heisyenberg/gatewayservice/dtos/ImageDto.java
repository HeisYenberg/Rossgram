package com.heisyenberg.gatewayservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {
  @JsonProperty("image_base64")
  private String imageBase64;

  @JsonProperty("user_id")
  private String userId;
}
