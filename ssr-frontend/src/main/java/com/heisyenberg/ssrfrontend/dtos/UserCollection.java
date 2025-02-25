package com.heisyenberg.ssrfrontend.dtos;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCollection {
  private Long id;
  private Set<String> imagesIds;
}
