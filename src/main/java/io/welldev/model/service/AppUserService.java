package io.welldev.model.service;

import io.welldev.model.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {
    public void save(AppUser credentials, String role);
    public AppUser findCredentialsByUsername(String username);

    List<AppUser> findAll();
}
