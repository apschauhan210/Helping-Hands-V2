package com.hh.helping_hands_as.repositories;

import com.hh.helping_hands_as.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    void deleteUserByUsername(String username);
}
