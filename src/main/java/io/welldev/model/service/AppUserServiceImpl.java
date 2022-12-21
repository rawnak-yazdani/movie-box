package io.welldev.model.service;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.AppUser;
import io.welldev.model.entity.Movie;
import io.welldev.model.repository.AppUserRepo;
import io.welldev.model.role.Roles;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepo appUserRepo;

    private final MovieService movieService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    @Override
    public List<AppUser> findAll() {
        return appUserRepo.findAll();
    }

    /**
     * This method will be called second during login [from authenticationManager.authenticate(authentication)]
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepo.findByUsername(username);
        if (user != null) {
            if (user.getRole().equals("user")) {
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(Roles.USER.grantedAuthorities())
                        .build();
            } else {
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(Roles.ADMIN.grantedAuthorities())
                        .build();
            }

        } else throw new UsernameNotFoundException(String.format("Username %s not found", username));

    }

    public void save(AppUser user, String role) {
        if (appUserRepo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException();
        } else {
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            appUserRepo.save(user);
        }

    }

    @Override
    public AppUserOutput updateUserInfo(AppUserInput appUserInput) {
        AppUser appUser = appUserRepo.findByUsername(appUserInput.getUsername());

        if (appUser != null) {
            mapper.map(appUserInput, appUser);
            appUser.setPassword(passwordEncoder.encode(appUserInput.getPassword()));
            appUserRepo.save(appUser);

            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(appUser, appUserOutput);
            return appUserOutput;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "User does not Exist"
            );
        }

    }

    @Override
    public AppUserOutput updateWatchlist(String reqUsername, List<UserMovieInput> userMovieInputs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            AppUser createdUser = appUserRepo.findByUsername(reqUsername);

            List<Movie> updatedList = new LinkedList<>();

            for (UserMovieInput input :
                    userMovieInputs) {
                updatedList.add(movieService.findById(input.getId()));
            }

            createdUser.getWatchList().addAll(updatedList);
            appUserRepo.save(createdUser);

            createdUser = appUserRepo.findByUsername(reqUsername);
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(createdUser, appUserOutput);

            return appUserOutput;
        } else return null;

    }

    @Override
    public AppUserOutput deleteFromWatchlist(String reqUsername, List<UserMovieInput> userMovieInputs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            AppUser createdUser = appUserRepo.findByUsername(reqUsername);

            for (UserMovieInput input :
                    userMovieInputs) {
                createdUser.getWatchList().remove(movieService.findById(input.getId()));
            }

            appUserRepo.save(createdUser);

            createdUser = appUserRepo.findByUsername(reqUsername);
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(createdUser, appUserOutput);

            return appUserOutput;
        } else return null;

    }

    public AppUser findAppUserByUsername(String username) {
        return appUserRepo.findByUsername(username);
    }

    @Override
    public AppUserOutput showAUser(String username) {
        Optional<AppUser> requestedAppUser = Optional.ofNullable(appUserRepo.findByUsername(username));

        if (requestedAppUser.isPresent()) {
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(requestedAppUser.get(), appUserOutput);
            return appUserOutput;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User does not Exist"
            );
        }
    }

    @Override
    public List<AppUserOutput> showAllUsers() {
        Optional<List<AppUser>> appUsers = Optional.of(appUserRepo.findAll());
        List<AppUserOutput> appUserOutputs = new LinkedList<>();

        for (AppUser user :
                appUsers.get()) {
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(user, appUserOutput);
            appUserOutputs.add(appUserOutput);
        }

        return appUserOutputs;
    }
}
