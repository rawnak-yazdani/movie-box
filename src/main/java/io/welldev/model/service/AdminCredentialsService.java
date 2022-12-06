package io.welldev.model.service;

import io.welldev.model.entity.AdminCredentials;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminCredentialsService extends UserDetailsService {
    public void save(AdminCredentials adminCredentials);
    public AdminCredentials getUserByName(String username);
}
