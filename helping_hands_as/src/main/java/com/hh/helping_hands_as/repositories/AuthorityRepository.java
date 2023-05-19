package com.hh.helping_hands_as.repositories;

import com.hh.helping_hands_as.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Optional<Authority> findAuthorityByName(String name);

}
