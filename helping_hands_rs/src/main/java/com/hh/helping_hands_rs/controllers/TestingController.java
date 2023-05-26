package com.hh.helping_hands_rs.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestingController {

    @GetMapping("/demo")
    public String demo(Authentication authentication) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = principal.getClaims();
        String fname = (String) claims.get("fname");
        String lname = (String) claims.get("lname");
        return "Demo";
    }
}
