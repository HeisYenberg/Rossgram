package com.heisyenberg.ssrfrontend.configs;

import static feign.FeignException.FeignClientException;

import com.heisyenberg.ssrfrontend.dtos.UserDto;
import com.heisyenberg.ssrfrontend.services.JwtService;
import com.heisyenberg.ssrfrontend.services.clients.AuthorizationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final AuthorizationServiceClient authorizationServiceClient;
  private final JwtService jwtService;

  @Override
  public Authentication authenticate(final Authentication authentication)
      throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();
    try {
      UserDto userDto = new UserDto(username, password);
      String token = authorizationServiceClient.authorize(userDto).getBody();
      String userId = jwtService.getUserIdFromToken(token);
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(userId, username, null);
      authenticationToken.setDetails(token);
      return authenticationToken;
    } catch (FeignClientException e) {
      throw new BadCredentialsException("Bad credentials", e);
    }
  }

  @Override
  public boolean supports(final Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
