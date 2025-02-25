package com.heisyenberg.commentsservice.exceptions;

public class CommentException extends RuntimeException {
  public CommentException(final String message) {
    super(message);
  }
}
