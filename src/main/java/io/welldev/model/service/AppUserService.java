package io.welldev.model.service;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {
    public AppUserOutput updateUserInfo(AppUserInput credentials);

    public AppUserOutput updateWatchlist(String reqUsername, List<UserMovieInput> userMovieInputs);

    public AppUserOutput deleteFromWatchlist(String reqUsername, List<UserMovieInput> userMovieInputs);

    public void save(AppUser credentials, String role);

    public AppUser findAppUserByUsername(String username);

    List<AppUser> findAll();
}
