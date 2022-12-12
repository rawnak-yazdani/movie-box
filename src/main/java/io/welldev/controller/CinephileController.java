package io.welldev.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.welldev.initializer.configuration.JwtSpecification;
import io.welldev.model.entity.Cinephile;
import io.welldev.model.entity.Movie;
import io.welldev.model.service.CinephileService;
import io.welldev.model.service.CredentialsService;
import io.welldev.model.service.GenreService;
import io.welldev.model.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping(value = "/users", headers = "Accept=application/json",
        produces = "application/json")
public class CinephileController {  

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

    @GetMapping(value = "/{id}")
    public Cinephile getUser(@PathVariable("id") Long id) {
        try {
            return userService.findById(id);
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
        String username = JwtSpecification.currentUsername;
        if (username.equals(reqUsername)) {
            Cinephile userCinephile = credentialsService.getUserByName(reqUsername).getCinephile();

            for (Movie m:
                 movies) {
                genreService.saveAll(m.getGenres());
            }

            movieService.saveAll(movies);
            userCinephile.getWatchList().addAll(movies);
            userService.save(userCinephile);

            return  new ResponseEntity<>(
                    credentialsService
                            .getUserByName(reqUsername)
                            .getCinephile(),
                    HttpStatus.ACCEPTED
            );
        } else return  new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }


}
