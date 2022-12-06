package io.welldev.model.service;

import io.welldev.model.entity.AppUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserDetailsService extends UserDetailsService {
    public void save(AppUserDetails appUserDetails);
    public AppUserDetails getUserByName(String username);
}
