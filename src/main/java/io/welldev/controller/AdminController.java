package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import io.welldev.model.constants.Constants.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = API.ADMIN, headers = Strings.HEADERS_JSON, produces = Strings.PRODUCES_JSON)
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;

    private final GenreService genreService;

    private final AppUserService appUserService;

    @PostMapping    // other admin signup
    public ResponseEntity<AppUserOutput> addOtherAdmin(@Valid @RequestBody AppUserInput appUserInput) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appUserService.adminSignUp(appUserInput));
    }

    @PostMapping(value = API.ADD_A_USER_BY_ADMIN)
    public ResponseEntity<AppUserOutput> addUser(@Valid @RequestBody AppUserInput appUserInput) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appUserService.userSignUp(appUserInput));
    }

    @PostMapping(API.ADD_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movieService.save(movie));
    }

    @PutMapping(API.UPDATE_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {
        return ResponseEntity
                .ok()
                .body(movieService.updateAMovieInfo(id, movie));
    }

    /*
    {
        "title":"Dhoom",
        "genres":[{"name":"Action"}, {"name":"Thriller"}],
        "rating":"8/10",
        "year":2004
    }

    {
        "title":"Dhoom 2",
        "genres":[{"name":"Action 2"}, {"name":"Thriller 2"}],
        "rating":"9/10",
        "year":2006
    }

    {
        "title":"Dhoom 3",
        "genres":[{"name":"Action 3"}, {"name":"Thriller 3"}],
        "rating":"7/10",
        "year":2013
    }
    */
    @DeleteMapping(API.DELETE_A_MOVIE_BY_ADMIN)
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
//        return HttpStatus.NO_CONTENT;
    }
}
