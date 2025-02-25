package com.heisyenberg.ssrfrontend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
  private Long id;
  private String userId;
  private String imageBase64;
  private boolean deleted;
}
