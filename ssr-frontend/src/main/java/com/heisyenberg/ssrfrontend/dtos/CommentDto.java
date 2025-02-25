package com.heisyenberg.ssrfrontend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
  @JsonProperty("comment_text")
  private String commentText;

  @JsonProperty("user_id")
  private String userId;
}
