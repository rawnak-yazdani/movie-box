package io.welldev.controller;

import io.welldev.model.entity.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/", headers = {"Accept=application/json"},
produces = "application/json")
public class HomeController {

    @GetMapping
    public List<Movie> getMovies() {
        return new ArrayList<Movie>();
    }
}
