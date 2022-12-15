package io.welldev.controller;

import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/admin", headers = {"Accept=application/json"},
        produces = "application/json")
public class AdminController {

    @Autowired
    MovieService movieService;

    @Autowired
    GenreService genreService;

    @Autowired
    AppUserService appUserService;

    List<DemoPurpose> movies = new ArrayList<>(Arrays.asList(
            new DemoPurpose(1, "Movie 1"),
            new DemoPurpose(2, "Movie 2")
    ));

    @GetMapping
    public List<DemoPurpose> getUser() {
//        return new User();
        return movies;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // other admin signup
    @PostMapping(value = "/signup")
    public ResponseEntity<AppUser> addOtherAdmin(@Valid @RequestBody AppUser appUser) {
        appUserService.save(appUser, "admin");
        AppUser createdAdmin = appUserService.findAppUserByUsername(appUser.getUsername());

        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    // add movies by admin
    @PostMapping("/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Movie> addMovie(@RequestBody Movie movie) {

        movieService.save(movie);

        return movieService.findAll();

    }

    // update movies by admin
    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {
        movie.setId(movieService.findById(id).getId());

        genreService.saveAll(movie.getGenres());
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
    // delete movies by admin
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<List<Movie>> deleteMovie(@PathVariable Long id) {

        movieService.deleteById(id);

        return new ResponseEntity<>(movieService.findAll(), HttpStatus.ACCEPTED);

    }

//    @PostMapping(value = "/signup")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Admin addUser(@RequestBody AdminCredentials adminCredentials) {
//        adminService.save(adminCredentials.getAdmin());
//        adminCredentialsService.save(adminCredentials);
//
//        return adminCredentialsService.getUserByName(adminCredentials.getUsername()).getAdmin();
//    }
}
