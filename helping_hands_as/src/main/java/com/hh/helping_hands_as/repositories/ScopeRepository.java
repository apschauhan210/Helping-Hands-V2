package com.hh.helping_hands_as.repositories;

import com.hh.helping_hands_as.entities.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, Integer> {

    Optional<Scope> findScopeByName(String name);
}
