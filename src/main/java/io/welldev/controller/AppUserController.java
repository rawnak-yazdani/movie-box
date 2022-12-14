package io.welldev.controller;

import io.welldev.model.entity.Cinephile;
import io.welldev.model.entity.AppUser;
import io.welldev.model.entity.Movie;
import io.welldev.model.service.AppUserService;
import io.welldev.model.service.GenreService;
import io.welldev.model.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping
    public List<AppUser> getUsers() {
        return appUserService.findAll();
    }

    @GetMapping(value = "/{username}")
    public AppUser getUser(@PathVariable("username") String username) {
        try {
            AppUser requestedUserCredentials = appUserService.findCredentialsByUsername(username);
            return requestedUserCredentials;
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User does not Exist"
            );
        }
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<AppUser> addToWatchList(@PathVariable("username") String reqUsername,
                                                    @RequestBody List<Movie> movies) {
//        String username = JwtSpecification.currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            AppUser createdUser = appUserService.findCredentialsByUsername(reqUsername);

            for (Movie m :
                    movies) {
                genreService.saveAll(m.getGenres());
            }

            movieService.saveAll(movies);
            createdUser.getWatchList().addAll(movies);
            appUserService.save(createdUser, createdUser.getRole());

            return new ResponseEntity<>(
                    appUserService
                            .findCredentialsByUsername(reqUsername),
                    HttpStatus.ACCEPTED
            );
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value = "/{username}/{id}")
    public ResponseEntity<AppUser> deleteFromWatchList(@PathVariable String username,
                                                         @PathVariable Long id) {
        try {
            AppUser requestedUser = appUserService.findCredentialsByUsername(username);
            for (Movie m:
                 requestedUser.getWatchList()) {
                if (m.getId() == id) requestedUser.getWatchList().remove(m);
            }
//            appUserService.save(cinephile);
            return new ResponseEntity<>(requestedUser, HttpStatus.OK);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or watchlist movie not found!");
        }

    }


}
