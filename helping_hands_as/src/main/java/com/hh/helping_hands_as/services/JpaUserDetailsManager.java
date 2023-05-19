package com.hh.helping_hands_as.services;

import com.hh.helping_hands_as.entities.Authority;
import com.hh.helping_hands_as.entities.User;
import com.hh.helping_hands_as.repositories.AuthorityRepository;
import com.hh.helping_hands_as.repositories.UserRepository;
import com.hh.helping_hands_as.security.SecurityAuthority;
import com.hh.helping_hands_as.security.SecurityUser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);

        return user.map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void createUser(UserDetails user) {
        SecurityUser securityUser = (SecurityUser) user;

        Optional<User> existingUser = userRepository.findUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("User with username " + user.getUsername() + " already exists");
        }

        if(user.getAuthorities() != null) {
            Collection<SecurityAuthority> userAuthorities = (Collection<SecurityAuthority>) user.getAuthorities();
            Set<Authority> modifiedAuthorities = new HashSet<>();
            for (SecurityAuthority a : userAuthorities) {
                String name = a.getAuthority();
                Optional<Authority> authority = authorityRepository.findAuthorityByName(name);
                if (authority.isPresent()) {
                    modifiedAuthorities.add(authority.get());
                } else {
                    modifiedAuthorities.add(new Authority(name));
                }
            }
            securityUser.getUser().setAuthorities(modifiedAuthorities);
        }
        userRepository.save(securityUser.getUser());
    }

    @Override
    public void updateUser(UserDetails user) {
        SecurityUser securityUser = (SecurityUser) user;
        User oldUser = userRepository.findUserByUsername(securityUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User newUser = securityUser.getUser();
        newUser.setId(oldUser.getId());
        userRepository.save(newUser);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteUserByUsername(username);
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = currentUser.getName();
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
            } else {
                throw new BadCredentialsException("Old password is wrong");
            }
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }
}
