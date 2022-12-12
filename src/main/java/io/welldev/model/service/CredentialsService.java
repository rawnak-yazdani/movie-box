package io.welldev.model.service;

import io.welldev.model.entity.Credentials;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CredentialsService extends UserDetailsService {
    public void save(Credentials credentials, String role);
    public Credentials getUserByName(String username);
}
