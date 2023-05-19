package com.hh.helping_hands_as.repositories;

import com.hh.helping_hands_as.entities.GrantType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrantTypeRepository extends JpaRepository<GrantType, Integer> {

    Optional<GrantType> findGrantTypeByName(String name);
}
