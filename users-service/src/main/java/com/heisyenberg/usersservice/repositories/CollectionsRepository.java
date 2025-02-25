package com.heisyenberg.usersservice.repositories;

import com.heisyenberg.usersservice.models.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionsRepository extends JpaRepository<UserCollection, Long> {}
