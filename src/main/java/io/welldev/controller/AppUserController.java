package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.constants.Constants.*;
import io.welldev.model.entity.AppUser;
import io.welldev.model.service.AppUserService;
import io.welldev.model.service.BlackListingService;
import io.welldev.model.service.GenreService;
import io.welldev.model.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = API.USERS, headers = Strings.HEADERS_JSON, produces = Strings.PRODUCES_JSON)
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    private final MovieService movieService;

    private final ModelMapper mapper;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BlackListingService blackListingService;

    @GetMapping     // show all users
    public ResponseEntity<List<AppUserOutput>> showAllUsers() {
        return ResponseEntity.ok(appUserService.showAllUsers());
    }

    @GetMapping(value = API.SHOW_A_USER)
    public ResponseEntity<AppUserOutput> showAUser(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(appUserService.showAUser(username));
    }

    // update watchlist or add movies to watchlist of a user
    @PutMapping(value = "/{username}/watchlist")
    public ResponseEntity<AppUserOutput> addMovieToWatchList(@PathVariable("username") String reqUsername,
                                                             @RequestBody List<UserMovieInput> userMovieInputs) {

        AppUserOutput appUserOutput = appUserService.updateWatchlist(reqUsername, userMovieInputs);
        return new ResponseEntity<>(appUserOutput, appUserOutput == null ? HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = API.DELETE_FROM_USER_WATCHLIST)
    public ResponseEntity<AppUserOutput> deleteMovieFromWatchList(@PathVariable String username,
                                                                  @RequestBody List<UserMovieInput> movieInputs) {

        AppUserOutput appUserOutput = appUserService.deleteFromWatchlist(username, movieInputs);
        return new ResponseEntity<>(appUserOutput, appUserOutput == null ? HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED);
    }

    @PutMapping     // update user info
    public ResponseEntity<AppUserOutput> editUser(@Valid @RequestBody AppUserInput appUserInput) {
        return new ResponseEntity<>(appUserService.updateUserInfo(appUserInput), HttpStatus.ACCEPTED);
    }

    @PutMapping("/logout")
    public ResponseEntity<Void> logout() {
        String jwtToken = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getDetails();
        blackListingService.blackListJwt(jwtToken);
        return ResponseEntity.ok(null);
    }

}
