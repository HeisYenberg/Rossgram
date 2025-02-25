package com.heisyenberg.gatewayservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
  private Long id;
  private String imageId;
  private String userId;
  private String text;
  private Boolean deleted;
}
