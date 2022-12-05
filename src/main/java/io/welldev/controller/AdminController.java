package io.welldev.controller;

import io.welldev.model.entity.DemoPurpose;
import io.welldev.model.entity.Genre;
import io.welldev.model.entity.Movie;
import io.welldev.model.service.GenreService;
import io.welldev.model.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/admin", headers = {"Accept=application/json"},
        produces = "application/json")
public class AdminController {

    @Autowired
    MovieService movieService;

    @Autowired
    GenreService genreService;

    List<DemoPurpose> movies = new ArrayList<>(Arrays.asList(
            new DemoPurpose(1, "Movie 1"),
            new DemoPurpose(2, "Movie 2")
    ));

    @GetMapping(value = "/users/{id}")
    public List<DemoPurpose> getUser(@PathVariable("id") Long id) {
//        return new User();
        return movies;
    }

    @PostMapping("/addMovie")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Movie> addMovie(@RequestBody Movie movie) {

        for (Genre genre :
                new ArrayList<>(movie.getGenres())) {
            genreService.saveAndFlush(genre);
        }

        movieService.saveAndFlush(movie);

        List<Movie> movies = movieService.findAll();

        return movies;

    }

/*
    {
        "id":null,
            "title":"Chehre",
            "genres":[{"id":null,"name":"Mystry"}, {"id":null,"name":"Thriller"}],
            "rating":"8/10",
            "year":2022
    }
*/

    @PutMapping("/addMovie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {

    }
}
