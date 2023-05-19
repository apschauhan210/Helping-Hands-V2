package com.hh.helping_hands_as.repositories;

import com.hh.helping_hands_as.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findClientByClientId(String clientId);
}
