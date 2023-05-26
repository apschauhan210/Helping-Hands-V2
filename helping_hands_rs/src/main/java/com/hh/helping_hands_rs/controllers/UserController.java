package com.hh.helping_hands_rs.controllers;

import com.hh.helping_hands_rs.services.EmployerService;
import com.hh.helping_hands_rs.services.HelperService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final HelperService helperService;

    private final EmployerService employerService;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('helper', 'employer')")
    public ResponseEntity registerUser(Authentication authentication) {
        try {
            List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) authentication.getAuthorities();
            if(authorities.contains(new SimpleGrantedAuthority("ROLE_helper"))) {
                helperService.registerHelper(authentication);
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_employer"))) {
                employerService.registerEmployer(authentication);
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
