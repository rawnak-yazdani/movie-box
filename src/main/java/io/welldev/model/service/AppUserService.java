package io.welldev.model.service;

import io.welldev.model.constants.Constants;
import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.AppUser;
import io.welldev.model.entity.Movie;
import io.welldev.model.exception.ItemNotFoundException;
import io.welldev.model.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class AppUserService {
    private final AppUserRepo appUserRepo;

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
        Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(appUserInput.getUsername()));

        if (appUser.isPresent()) {
            mapper.map(appUserInput, appUser);
            appUser.get().setPassword(passwordEncoder.encode(appUserInput.getPassword()));
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(appUserRepo.save(appUser.get()), appUserOutput);
            return appUserOutput;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User does not Exist");
        }

    }

    public AppUserOutput updateWatchlist(String reqUsername, List<UserMovieInput> userMovieInputs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            Optional<AppUser> createdUser = Optional.ofNullable(appUserRepo.findByUsername(reqUsername));

            List<Movie> updatedList = new LinkedList<>();

            for (UserMovieInput input :
                    userMovieInputs) {
                updatedList.add(movieService.findById(input.getId()));
            }

            if (createdUser.isPresent()) {

                createdUser.get().getWatchList().addAll(updatedList);
                AppUserOutput appUserOutput = new AppUserOutput();
                mapper.map(appUserRepo.save(createdUser.get()), appUserOutput);

                return appUserOutput;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
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
                    createdUser.get().getWatchList().remove(movieService.findById(input.getId()));
                }

                AppUserOutput appUserOutput = new AppUserOutput();
                mapper.map(appUserRepo.save(createdUser.get()), appUserOutput);

                return appUserOutput;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
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
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
            throw new ItemNotFoundException("User " + username+ " not Exist");
        }
    }

    public AppUserOutput showAUser(String username) {
        Optional<AppUser> requestedAppUser = Optional.ofNullable(appUserRepo.findByUsername(username));

        if (requestedAppUser.isPresent()) {
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(requestedAppUser.get(), appUserOutput);
            return appUserOutput;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
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

    public void deleteUser(AppUserInput appUserInput) {
        Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(appUserInput.getUsername()));

        if (appUser.isPresent())
            appUserRepo.delete(appUser.get());
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
    }
}
