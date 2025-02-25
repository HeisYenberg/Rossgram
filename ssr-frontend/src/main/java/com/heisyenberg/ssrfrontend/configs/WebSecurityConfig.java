package com.heisyenberg.ssrfrontend.configs;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      final HttpSecurity http, final AuthenticationProvider authenticationProvider)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authenticationProvider(authenticationProvider)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/authorization", "/registration")
                    .permitAll()
                    .requestMatchers(POST, "/users/me/collections", "/images/*/comments")
                    .authenticated()
                    .requestMatchers(DELETE, "/users/me/collections", "/images/*/comments/*")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .formLogin(
            form ->
                form.loginPage("/authorization")
                    .loginProcessingUrl("/authorization")
                    .defaultSuccessUrl("/users", true)
                    .permitAll())
        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/users").permitAll());
    return http.build();
  }
}
