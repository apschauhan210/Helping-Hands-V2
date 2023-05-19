package com.hh.helping_hands_as.services;

import com.hh.helping_hands_as.entities.Client;
import com.hh.helping_hands_as.entities.GrantType;
import com.hh.helping_hands_as.entities.Scope;
import com.hh.helping_hands_as.repositories.ClientRepository;
import com.hh.helping_hands_as.repositories.GrantTypeRepository;
import com.hh.helping_hands_as.repositories.ScopeRepository;
import com.hh.helping_hands_as.security.SecurityClient;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientRepository clientRepository;
    private final ScopeRepository scopeRepository;
    private final GrantTypeRepository grantTypeRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        SecurityClient securityClient = (SecurityClient) registeredClient;

        Optional<Client> existingClient = clientRepository.findClientByClientId(securityClient.getClientId());
        if(existingClient.isPresent()) {
            throw new IllegalStateException("Client with clientId " + securityClient.getClientId() + " already exists");
        }

        Set<String> scopes = securityClient.getScopes();
        Set<AuthorizationGrantType> grantTypes = securityClient.getAuthorizationGrantTypes();
        Set<Scope> modifiedScopes = new HashSet<>();
        Set<GrantType> modifiedGrantTypes = new HashSet<>();

        for (String scope : scopes) {
            Optional<Scope> existingScope = scopeRepository.findScopeByName(scope);
            if(existingScope.isPresent()) {
                modifiedScopes.add(existingScope.get());
            } else {
                modifiedScopes.add(new Scope(scope));
            }
        }
        securityClient.getClient().setScopes(modifiedScopes);

        for (AuthorizationGrantType grantType : grantTypes) {
            Optional<GrantType> existingGrantType = grantTypeRepository.findGrantTypeByName(grantType.getValue());
            if(existingGrantType.isPresent()) {
                modifiedGrantTypes.add(existingGrantType.get());
            } else {
                modifiedGrantTypes.add(new GrantType(grantType.getValue()));
            }
        }
        securityClient.getClient().setGrantTypes(modifiedGrantTypes);

        clientRepository.save(securityClient.getClient());
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findById(Integer.parseInt(id))
                .map(SecurityClient::new)
                .orElseThrow(() -> new IllegalStateException("Client not found with id " + id));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findClientByClientId(clientId)
                .map(SecurityClient::new)
                .orElseThrow(() -> new IllegalStateException("Client with clientId " + clientId + " is not found"));
    }
}
