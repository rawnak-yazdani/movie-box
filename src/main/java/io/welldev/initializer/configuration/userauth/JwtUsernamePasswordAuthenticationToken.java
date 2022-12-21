package io.welldev.initializer.configuration.userauth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public JwtUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public JwtUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
