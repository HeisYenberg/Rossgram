package com.heisyenberg.usersservice.services;

import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserCollection;
import com.heisyenberg.usersservice.models.UserDto;
import com.heisyenberg.usersservice.repositories.CollectionsRepository;
import com.heisyenberg.usersservice.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersService {

  private final UsersRepository usersRepository;
  private final CollectionsRepository collectionsRepository;

  public User createUser(@NotNull final UserDto userDto) {
    if (usersRepository.existsByUsername(userDto.getUsername())) {
      throw new UserException("Username already exists");
    }
    UserCollection collection = new UserCollection();
    collection = collectionsRepository.save(collection);
    User user =
        User.builder()
            .username(userDto.getUsername())
            .password(userDto.getPassword())
            .userCollection(collection)
            .build();
    return usersRepository.save(user);
  }

  public String getUsernameById(@NotNull final Long id) {
    return usersRepository
        .findById(id)
        .orElseThrow(() -> new UserException("User not found"))
        .getUsername();
  }

  public Page<User> getUsersPage(final Integer limit, final Integer skip) {
    return usersRepository.findAll(PageRequest.of(skip, limit));
  }

  public User getByUsername(final String username) {
    return usersRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserException("User not found"));
  }

  public UserDetailsService userDetailsService() {
    return this::getByUsername;
  }
}
