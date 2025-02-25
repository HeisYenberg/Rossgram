package com.heisyenberg.ssrfrontend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(Exception.class)
  public String handleGlobalException(final HttpServletRequest request) {
    String referer = request.getHeader("Referer");
    return referer != null ? "redirect:" + referer : "redirect:/";
  }
}
