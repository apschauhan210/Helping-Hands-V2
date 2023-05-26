package com.hh.helping_hands_rs.services;

import com.hh.helping_hands_rs.entities.Helper;
import com.hh.helping_hands_rs.repositories.HelperRepository;
import jakarta.transaction.Transactional;
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
public class HelperService {

    private final HelperRepository helperRepository;

    public Helper findHelperByEmail(String email) {
        Optional<Helper> helperOptional = helperRepository.findHelperByEmail(email);
        return helperOptional.orElseThrow(() -> new UsernameNotFoundException(email + " is not registered"));
    }

    public void registerHelper(Authentication authentication) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = principal.getClaims();
        String name = claims.get("fname") + " " + claims.get("lname");
        Helper helper = new Helper(authentication.getName(), name, null, new HashSet<>(), new HashSet<>(), new HashSet<>());
        Optional<Helper> helperByEmail = helperRepository.findHelperByEmail(helper.getEmail());
        if (helperByEmail.isEmpty())
            helperRepository.save(helper);
    }
}
