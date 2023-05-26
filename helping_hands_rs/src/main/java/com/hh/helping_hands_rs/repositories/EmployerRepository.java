package com.hh.helping_hands_rs.repositories;

import com.hh.helping_hands_rs.entities.Employer;
import com.hh.helping_hands_rs.entities.Helper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

    Optional<Employer> findEmployerByEmail(String email);

//    Optional<List<Helper>> findAllByEmail(String email);
}
