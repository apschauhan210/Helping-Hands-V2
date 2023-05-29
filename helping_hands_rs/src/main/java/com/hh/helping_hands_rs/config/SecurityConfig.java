package com.hh.helping_hands_rs.config;

import com.hh.helping_hands_rs.config.utils.AuthorityConversionJwtAuthenticationConverter;
import com.hh.helping_hands_rs.config.utils.CorsCustomizer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsCustomizer corsCustomizer;

    private final AuthorityConversionJwtAuthenticationConverter jwtAuthenticationConverter;

    @Value("${jwks.uri}")
    private String jwksUri;

    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(
                r -> r.jwt().jwkSetUri(jwksUri)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter)
        );

        http.authorizeHttpRequests().anyRequest().permitAll();

        corsCustomizer.corsCustomizer(http);

        return http.build();
    }
}
