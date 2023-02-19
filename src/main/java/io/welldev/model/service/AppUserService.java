package io.welldev.model.service;

import io.welldev.model.constants.Constants;
import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.dataoutputobject.MovieOutput;
import io.welldev.model.entity.AppUser;
import io.welldev.model.entity.Movie;
import io.welldev.model.exception.ItemNotFoundException;
import io.welldev.model.repository.AppUserRepo;
import io.welldev.model.repository.MovieRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {
    private final AppUserRepo appUserRepo;

    private  final MovieRepo movieRepo;

    private final MovieService movieService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    public List<AppUser> findAll() {
        return appUserRepo.findAll();
    }

    public AppUser save(AppUser user, String role) {
        Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(user.getUsername()));

        if (appUser.isPresent()) {
            throw new IllegalArgumentException();
        } else {
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return appUserRepo.save(user);
        }
    }

    public AppUserOutput signUp(AppUserInput appUserInput, String role) {
        AppUser appUser = new AppUser();
        mapper.map(appUserInput, appUser);

        try {
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(save(appUser, role), appUserOutput);
            return appUserOutput;
        } catch (IllegalArgumentException argumentException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
    }

    public AppUserOutput userSignUp(AppUserInput appUserInput) {
        return signUp(appUserInput, Constants.AppStrings.USER_ROLE);
    }

    public AppUserOutput adminSignUp(AppUserInput appUserInput) {
        return signUp(appUserInput, Constants.AppStrings.ADMIN_ROLE);
    }

    public AppUserOutput updateUserInfo(AppUserInput appUserInput) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals(appUserInput.getUsername())) {
            Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(appUserInput.getUsername()));

            if (appUser.isPresent()) {
                appUser.get().setName(appUserInput.getName());
                appUser.get().setUsername(appUserInput.getUsername());
                appUser.get().setPassword(passwordEncoder.encode(appUserInput.getPassword()));
                AppUserOutput appUserOutput = new AppUserOutput();
                mapper.map(appUserRepo.save(appUser.get()), appUserOutput);
                return appUserOutput;
            } else {
                throw new ItemNotFoundException("User does not exist!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

    }

    public AppUserOutput updateWatchlist(String reqUsername, List<UserMovieInput> userMovieInputs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            Optional<AppUser> createdUser = Optional.ofNullable(appUserRepo.findByUsername(reqUsername));

            List<Movie> updatedList = new LinkedList<>();

            for (UserMovieInput input :
                    userMovieInputs) {
                updatedList.add(movieRepo.findById(input.getId()).get());
            }

            if (createdUser.isPresent()) {

                createdUser.get().getWatchlist().addAll(updatedList);
                AppUserOutput appUserOutput = new AppUserOutput();
                mapper.map(appUserRepo.save(createdUser.get()), appUserOutput);

                return appUserOutput;
            } else {
                throw new ItemNotFoundException("User does not exist!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

    }

    public AppUserOutput deleteFromWatchlist(String reqUsername, List<UserMovieInput> userMovieInputs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            Optional<AppUser> createdUser = Optional.ofNullable(appUserRepo.findByUsername(reqUsername));

            if (createdUser.isPresent()) {
                for (UserMovieInput input :
                        userMovieInputs) {
                    createdUser.get().getWatchlist().remove(movieRepo.findById(input.getId()).get());
                }

                AppUserOutput appUserOutput = new AppUserOutput();
                mapper.map(appUserRepo.save(createdUser.get()), appUserOutput);

                return appUserOutput;
            } else {
                throw new ItemNotFoundException("User does not exist!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }


    }

    public AppUser findAppUserByUsername(String username) {
        Optional<AppUser> requestedAppUser = Optional.ofNullable(appUserRepo.findByUsername(username));

        if (requestedAppUser.isPresent())
            return appUserRepo.findByUsername(username);
        else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
//            throw new UsernameNotFoundException("Username or Password incorrect");
            throw new ItemNotFoundException("Username or Password is Wrong. Otherwise the user is needed to sign up.");
        }
    }

    public AppUserOutput showAUser(String username) throws IOException {
        Optional<AppUser> requestedAppUser = Optional.ofNullable(appUserRepo.findByUsername(username));

        if (requestedAppUser.isPresent()) {
            AppUserOutput appUserOutput = new AppUserOutput();
            Set<MovieOutput> movieOutputs = new HashSet<>();
            for ( Movie movie : requestedAppUser.get().getWatchlist()) {
                movieOutputs.add(movieService.mapMovie(movie));
            }
            mapper.map(requestedAppUser.get(), appUserOutput);
            appUserOutput.setWatchlist(movieOutputs);
            return appUserOutput;
        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
            throw new ItemNotFoundException("User does not exist!");
        }
    }

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

    public void deleteUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(username)) {
            Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(username));

            if (appUser.isPresent())
                appUserRepo.delete(appUser.get());
            else
                throw new ItemNotFoundException("User does not exist!");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

    }
}
