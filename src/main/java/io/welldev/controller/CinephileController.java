package io.welldev.controller;

import io.welldev.model.entity.Cinephile;
import io.welldev.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users", headers = "Accept=application/json",
        produces = "application/json")
public class CinephileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Cinephile> getUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Cinephile getUser(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody Cinephile newCinephile) {
        userService.save(newCinephile);
        return "redirect:/";
    }


}
