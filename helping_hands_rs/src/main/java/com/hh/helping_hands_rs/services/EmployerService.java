package com.hh.helping_hands_rs.services;

import com.hh.helping_hands_rs.entities.Employer;
import com.hh.helping_hands_rs.repositories.EmployerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployerService {

    private final EmployerRepository employerRepository;

    public Employer findEmployerByEmail(String email) {
        Optional<Employer> employerOptional = employerRepository.findEmployerByEmail(email);
        return employerOptional.orElseThrow(() -> new UsernameNotFoundException(email + " is not registered!"));
    }

    public void registerEmployer(Authentication authentication) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = principal.getClaims();
        String name = claims.get("fname") + " " + claims.get("lname");
        Employer employer = new Employer(
                authentication.getName(),  // email(username) is the name in authentication object
                name,
                null,
                new HashSet<>()
        );
        Optional<Employer> employerByEmail = employerRepository.findEmployerByEmail(employer.getEmail());
        if (employerByEmail.isEmpty())
            employerRepository.save(employer);
    }

}
