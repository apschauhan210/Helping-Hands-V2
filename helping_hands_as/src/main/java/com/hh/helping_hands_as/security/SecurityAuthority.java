package com.hh.helping_hands_as.security;

import com.hh.helping_hands_as.entities.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
@Setter
public class SecurityAuthority implements GrantedAuthority {

    private final Authority authority;

    @Override
    public String getAuthority() {
        return authority.getName();
    }
}
