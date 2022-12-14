package io.welldev.controller;

import io.welldev.model.entity.Cinephile;
import io.welldev.model.entity.Credentials;
import io.welldev.model.entity.Movie;
import io.welldev.model.service.CinephileService;
import io.welldev.model.service.CredentialsService;
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
public class UserController {

    @Autowired
    private CinephileService userService;
    @Autowired
    private CredentialsService credentialsService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private GenreService genreService;

    @GetMapping
    public List<Cinephile> getUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "/{username}")
    public Cinephile getUser(@PathVariable("username") String username) {
        try {
            Credentials requestedUserCredentials = credentialsService.findCredentialsByUsername(username);
            return requestedUserCredentials.getCinephile();
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User does not Exist"
            );
        }
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<Cinephile> addToWatchList(@PathVariable("username") String reqUsername,
                                                    @RequestBody List<Movie> movies) {
//        String username = JwtSpecification.currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            Cinephile userCinephile = credentialsService.findCredentialsByUsername(reqUsername).getCinephile();

            for (Movie m :
                    movies) {
                genreService.saveAll(m.getGenres());
            }

            movieService.saveAll(movies);
            userCinephile.getWatchList().addAll(movies);
            userService.save(userCinephile);

            return new ResponseEntity<>(
                    credentialsService
                            .findCredentialsByUsername(reqUsername)
                            .getCinephile(),
                    HttpStatus.ACCEPTED
            );
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value = "/{username}/{id}")
    public ResponseEntity<Cinephile> deleteFromWatchList(@PathVariable String username,
                                                         @PathVariable Long id) {
        try {
            Credentials requestedUserCredentials = credentialsService.findCredentialsByUsername(username);
            Cinephile cinephile = requestedUserCredentials.getCinephile();
            for (Movie m:
                 cinephile.getWatchList()) {
                if (m.getId() == id) cinephile.getWatchList().remove(m);
            }
//            userService.save(cinephile);
            return new ResponseEntity<>(requestedUserCredentials.getCinephile(), HttpStatus.OK);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or watchlist movie not found!");
        }

    }


}
