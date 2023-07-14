package com.hh.helping_hands_as.config;

import com.hh.helping_hands_as.config.utils.CorsCustomizer;
import com.hh.helping_hands_as.security.SecurityUser;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsCustomizer corsCustomizer;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        http.exceptionHandling(e -> {
            e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        });

        corsCustomizer.corsCustomizer(http);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
//                .formLogin(Customizer.withDefaults())
                .formLogin(e -> {                                 // custom login page
                    e.loginPage("/login");
                    e.loginProcessingUrl("/process-login");
                    e.defaultSuccessUrl("/", false);
                    e.failureUrl("/login?error=true");
                })
                .logout(e -> {                                    // custom logout handling
                    e.logoutUrl("/logout");
                    e.deleteCookies("JSESSIONID");
                    e.logoutSuccessUrl("/");
                })
                .httpBasic()
                .and()
                .csrf().disable()
                .authorizeHttpRequests().requestMatchers("/client/register/**", "/user/change_password", "/secured/**").authenticated()
                .anyRequest().permitAll();

        corsCustomizer.corsCustomizer(http);

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRSAKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    public static KeyPair generateRSAKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return context -> {
            List<GrantedAuthority> authorityList = (List<GrantedAuthority>) context.getPrincipal().getAuthorities();
            context.getClaims().claim("authorities", authorityList.stream().map(GrantedAuthority::getAuthority).toList());

            if (context.getPrincipal().getPrincipal().getClass().equals(SecurityUser.class)) {
                SecurityUser authenticatedUser = (SecurityUser) context.getPrincipal().getPrincipal();
                context.getClaims().claim("fname", authenticatedUser.getUser().getFirstName());
                context.getClaims().claim("lname", authenticatedUser.getUser().getLastName());
            }
        };
    }
}

