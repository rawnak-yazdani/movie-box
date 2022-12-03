package io.welldev.controller;

import io.welldev.model.entity.DemoPurpose;
import io.welldev.model.entity.Genre;
import io.welldev.model.entity.Movie;
import io.welldev.model.service.GenreService;
import io.welldev.model.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/", headers = {"Accept=application/json"},
        produces = "application/json")
public class HomeController {

    @Autowired
    MovieService movieService;
//    List<Movie> movies = movieService.findAll();

    @Autowired
    GenreService genreService;

    List<DemoPurpose> movies = new ArrayList<>(Arrays.asList(
            new DemoPurpose(1, "Movie 1"),
            new DemoPurpose(2, "Movie 2")
    ));

    @GetMapping
    public List<Movie> getMovies() {

        List<Movie> movies = movieService.findAll();
//        for (Movie movie:
//                movies) {
//            System.out.println(movie.getTitle());
//        }

        return movies;
//        return new ArrayList<Movie>();
    }

    @GetMapping("/add")
    public List<Movie> addMovie() {

        Genre genre1 = new Genre();
        genre1.setName("Genre 1");

        Genre genre2 = new Genre();
        genre2.setName("Genre 2");

        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        genres.add(genre2);

        genreService.saveAndFlush(genre1);
        genreService.saveAndFlush(genre2);

        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setRating("10/10");
        movie1.setYear(2022);
        movie1.setGenres(new HashSet<>(genres));

        movieService.saveAndFlush(movie1);

        List<Movie> movies = movieService.findAll();

        return movies;
//        return new ArrayList<Movie>();
    }
}
