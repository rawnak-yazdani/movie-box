package io.welldev.controller;

import io.welldev.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users", headers = "Accept=application/json",
produces = "application/json")
public class UserController {

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return new User();
    }

    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody User newUser) {

    }


}
