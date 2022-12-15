package io.welldev.controller;

import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.AppUser;
import io.welldev.model.entity.Movie;
import io.welldev.model.service.AppUserService;
import io.welldev.model.service.GenreService;
import io.welldev.model.service.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/users", headers = "Accept=application/json",
        produces = "application/json")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<AppUserOutput>> getUsers() {
        mapper = new ModelMapper();
        List<AppUser> appUsers = appUserService.findAll();
        List<AppUserOutput> appUserOutputs = new LinkedList();
        for (AppUser user:
             appUsers) {
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(user, appUserOutput);
            appUserOutputs.add(appUserOutput);
        }
        return new ResponseEntity<>(
                appUserOutputs, HttpStatus.OK);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<AppUserOutput> getUser(@PathVariable("username") String username) {
        try {
            AppUser requestedAppUser = appUserService.findAppUserByUsername(username);
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(requestedAppUser, appUserOutput);
            return new ResponseEntity<>(appUserOutput, HttpStatus.FOUND);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User does not Exist"
            );
        }
    }

    @PutMapping(value = "/{username}/watchlist")
    public ResponseEntity<AppUserOutput> addToWatchList(@PathVariable("username") String reqUsername,
                                                        @RequestBody List<UserMovieInput> userMovieInputs) {
//        String username = JwtSpecification.currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            AppUser createdUser = appUserService.findAppUserByUsername(reqUsername);

            List<Movie> updatedList = new LinkedList<>();
            for (UserMovieInput input:
                 userMovieInputs) {
                updatedList.add(movieService.findById(input.getId()));
            }
            createdUser.getWatchList().addAll(updatedList);
            appUserService.save(createdUser, createdUser.getRole());

            createdUser = appUserService
                    .findAppUserByUsername(reqUsername);
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(createdUser, appUserOutput);
            return new ResponseEntity<>(
                    appUserOutput,
                    HttpStatus.ACCEPTED
            );
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value = "/{username}/watchlist")
    public ResponseEntity<AppUserOutput> deleteFromWatchList(@PathVariable String username,
                                                         @PathVariable List<UserMovieInput> movieInputs) {
        try {
            AppUser requestedUser = appUserService.findAppUserByUsername(username);
            for (UserMovieInput m:
                 movieInputs) {
                requestedUser.getWatchList().remove(movieService.findById(m.getId()));
            }
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(requestedUser, appUserOutput);
//            appUserService.save(cinephile);
            return new ResponseEntity<>(appUserOutput, HttpStatus.OK);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or watchlist movie not found!");
        }

    }


}
