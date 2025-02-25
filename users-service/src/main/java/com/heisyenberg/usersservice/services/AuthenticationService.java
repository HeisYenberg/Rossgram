package com.heisyenberg.usersservice.services;

import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {
  private final UsersService usersService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public String authenticate(final UserDto userDto) {
    String username = userDto.getUsername().toLowerCase();
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, userDto.getPassword()));
    final UserDetails user = usersService.userDetailsService().loadUserByUsername(username);
    return jwtService.generateToken(user);
  }

  public String registerUser(final UserDto userDto) {
    UserDto encodedUser =
        UserDto.builder()
            .username(userDto.getUsername().toLowerCase())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .build();
    User user = usersService.createUser(encodedUser);
    return jwtService.generateToken(user);
  }
}
