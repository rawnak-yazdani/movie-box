package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import io.welldev.model.constants.Constants.*;
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
public class AdminController {

    @Autowired
    MovieService movieService;

    @Autowired
    GenreService genreService;

    @Autowired
    AppUserService appUserService;

    @PostMapping    // other admin signup
    public ResponseEntity<AppUser> addOtherAdmin(@Valid @RequestBody AppUser appUser) {
        appUserService.save(appUser, "admin");
        AppUser createdAdmin = appUserService.findAppUserByUsername(appUser.getUsername());

        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @PostMapping(value = API.ADD_A_USER_BY_ADMIN)
    public ResponseEntity<AppUserOutput> addUser(@Valid @RequestBody AppUserInput appUserInput) {

        AppUser appUser = new AppUser();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(appUserInput, appUser);

        try {
            appUserService.save(appUser, "user");
            AppUser createdAppUser = appUserService.findAppUserByUsername(appUser.getUsername());
            AppUserOutput appUserOutput = new AppUserOutput();
            modelMapper.map(createdAppUser, appUserOutput);

            return new ResponseEntity<>(appUserOutput, HttpStatus.CREATED);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username was not saved Properly");
        } catch (IllegalArgumentException argumentException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
    }

    @PostMapping(API.ADD_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.save(movie));
    }

    @PutMapping(API.UPDATE_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {
        movie.setId(movieService.findById(id).getId());
        movieService.save(movie);

        return new ResponseEntity<>(movie, HttpStatus.OK);
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
    public ResponseEntity<List<Movie>> deleteMovie(@PathVariable Long id) {

        movieService.deleteById(id);

        return new ResponseEntity<>(movieService.findAll(), HttpStatus.ACCEPTED);

    }
}
