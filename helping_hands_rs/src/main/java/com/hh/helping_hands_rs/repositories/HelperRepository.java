package com.hh.helping_hands_rs.repositories;

import com.hh.helping_hands_rs.entities.Helper;
import com.hh.helping_hands_rs.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelperRepository extends JpaRepository<Helper, Long> {

    Optional<Helper> findHelperByEmail(String email);

    Optional<Helper> findHelpersByAddressToWork(Address address);
}
