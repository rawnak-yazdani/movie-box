package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.MovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import io.welldev.model.constants.Constants.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = API.ADMIN, headers = AppStrings.HEADERS_JSON, produces = AppStrings.PRODUCES_JSON)
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;

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
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody MovieInput movieInput) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movieService.addMovie(movieInput));
    }

    @PutMapping(API.UPDATE_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id,
                                             @Valid @RequestBody MovieInput movieInput) {
        return ResponseEntity
                .ok()
                .body(movieService.updateAMovieInfo(id, movieInput));
    }

    @DeleteMapping(API.DELETE_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Object> deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
