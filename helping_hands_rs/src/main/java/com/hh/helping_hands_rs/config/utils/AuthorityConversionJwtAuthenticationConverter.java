package com.hh.helping_hands_rs.config.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorityConversionJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        List<String> authorities = (List<String>) jwt.getClaim("authorities");
        JwtAuthenticationToken jwtAuthentication =
                new JwtAuthenticationToken(
                        jwt,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
                );

        return jwtAuthentication;
    }
}
