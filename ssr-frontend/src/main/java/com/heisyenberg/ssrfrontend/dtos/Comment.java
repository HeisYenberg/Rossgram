package com.heisyenberg.ssrfrontend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
  private Long id;
  private String imageId;
  private String userId;
  private String username;
  private String text;
  private Boolean deleted;
}
