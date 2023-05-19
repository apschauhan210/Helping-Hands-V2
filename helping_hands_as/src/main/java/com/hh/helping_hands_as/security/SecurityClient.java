package com.hh.helping_hands_as.security;

import com.hh.helping_hands_as.entities.Client;
import com.hh.helping_hands_as.entities.Scope;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class SecurityClient extends RegisteredClient {

    private final Client client;

    @Override
    public String getId() {
        return String.valueOf(client.getId());
    }

    @Override
    public String getClientId() {
        return client.getClientId();
    }

    @Override
    public String getClientSecret() {
        return client.getSecret();
    }

    @Override
    public Set<ClientAuthenticationMethod> getClientAuthenticationMethods() {
        return new HashSet<>(List.of(new ClientAuthenticationMethod(client.getAuthenticationMethod())));
    }

    @Override
    public Set<AuthorizationGrantType> getAuthorizationGrantTypes() {
        return client.getGrantTypes()
                .stream()
                .map((e) -> new AuthorizationGrantType(e.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getRedirectUris() {
        return client.getRedirectUris();
    }

    @Override
    public Set<String> getScopes() {
        return client.getScopes()
                .stream()
                .map(Scope::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public ClientSettings getClientSettings() {
        return ClientSettings.builder().build();
    }

    @Override
    public TokenSettings getTokenSettings() {
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(120))
                .build();
    }
}
