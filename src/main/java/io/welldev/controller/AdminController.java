package io.welldev.controller;

import io.welldev.model.entity.Movie;
import io.welldev.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin", headers = {"Accept=application/json"},
        produces = "application/json")
public class AdminController {

    @GetMapping(value = "/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return  new User();
    }

    @PostMapping("/addMovie")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMovie(@RequestBody Movie movie) {


    }

    @PutMapping("/addMovie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {

    }
}
