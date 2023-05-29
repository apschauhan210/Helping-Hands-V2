package com.hh.helping_hands_rs.repositories;

import com.hh.helping_hands_rs.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findJobByName(String name);
}
