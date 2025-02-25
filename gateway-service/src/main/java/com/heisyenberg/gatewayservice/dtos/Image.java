package com.heisyenberg.gatewayservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Image {
  private Long id;
  private String userId;
  private String imageBase64;
  private boolean deleted;
}
