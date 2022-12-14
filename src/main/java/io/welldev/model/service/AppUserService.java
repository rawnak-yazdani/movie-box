package io.welldev.model.service;

import io.welldev.model.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    public void save(AppUser credentials, String role);
    public AppUser findCredentialsByUsername(String username);
}
