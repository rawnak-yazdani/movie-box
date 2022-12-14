package io.welldev.controller;

import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    CredentialsService credentialsService;

    @Autowired
    AdminService adminService;

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
    // add movies
    @PostMapping("/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Movie> addMovie(@RequestBody Movie movie) {

        for (Genre genre :
                new ArrayList<>(movie.getGenres())) {
            genreService.saveAndFlush(genre);
        }

        movieService.saveAndFlush(movie);

        return movieService.findAll();

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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // delete movies
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<List<Movie>> deleteMovie(@PathVariable Long id) {

        movieService.deleteById(id);

        return new ResponseEntity<>(movieService.findAll(), HttpStatus.ACCEPTED);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // secondary admin signup
    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Admin> addUser(@Valid @RequestBody Credentials credentials) {
        credentialsService.save(credentials, credentials.getRole());
        Admin createdAdmin = credentialsService.findCredentialsByUsername(credentials.getUsername()).getAdmin();


        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PutMapping("/add-movie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {

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
