package io.welldev.model.service;

import io.welldev.model.entity.CinephileCredentials;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CinephileCredentialsService extends UserDetailsService {
    public void save(CinephileCredentials cinephileCredentials);
    public CinephileCredentials getUserByName(String username);
}
