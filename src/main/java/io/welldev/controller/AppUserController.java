package io.welldev.controller;

import io.welldev.model.constants.Constants.API;
import io.welldev.model.constants.Constants.AppStrings;
import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.service.AppUserService;
import io.welldev.model.service.BlackListingService;
import io.welldev.model.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = API.USERS, headers = AppStrings.HEADERS_JSON, produces = AppStrings.PRODUCES_JSON)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AppUserController {
    private final AppUserService appUserService;

    private final MovieService movieService;

    private final ModelMapper mapper;

    private final BlackListingService blackListingService;

    @GetMapping     // show all users
    public ResponseEntity<List<AppUserOutput>> showAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appUserService.showAllUsers());
    }

    @GetMapping(value = API.SHOW_A_USER)
    public ResponseEntity<AppUserOutput> showAUser(@PathVariable("username") String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appUserService.showAUser(username));
    }

    @PutMapping(value = API.UPDATE_USER_WATCHLIST)
    public ResponseEntity<AppUserOutput> addMovieToWatchList(@PathVariable("username") String reqUsername,
                                                             @RequestBody List<UserMovieInput> userMovieInputs) {
        return ResponseEntity
                .ok()
                .body(appUserService.updateWatchlist(reqUsername, userMovieInputs));
    }

    @DeleteMapping(value = API.DELETE_FROM_USER_WATCHLIST)
    public ResponseEntity<AppUserOutput> deleteMovieFromWatchList(@PathVariable("username") String reqUsername,
                                                                  @RequestBody List<UserMovieInput> userMovieInputs) {
        return ResponseEntity
                .ok()
                .body(appUserService.deleteFromWatchlist(reqUsername, userMovieInputs));
    }

    @PutMapping     // update user info
    public ResponseEntity<AppUserOutput> editUser(@Valid @RequestBody AppUserInput appUserInput) {
        return ResponseEntity
                .ok()
                .body(appUserService.updateUserInfo(appUserInput));
    }

    @PostMapping    // user sign up
    public ResponseEntity<AppUserOutput> addUser(@Valid @RequestBody AppUserInput appUserInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appUserService.userSignUp(appUserInput));
    }

    @PutMapping(API.LOGOUT_A_USER)
    public ResponseEntity<Void> logout() {
        String jwtToken = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        blackListingService.blackListJwt(jwtToken);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping(API.DELETE_USER)
    public ResponseEntity<Object> deleteAUser(@Valid @PathVariable String username) {
        appUserService.deleteUser(username);
        String jwtToken = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        blackListingService.blackListJwt(jwtToken);
        return ResponseEntity
                .noContent()
                .build();
    }

}
