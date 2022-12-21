package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.constants.Constants.*;
import io.welldev.model.entity.AppUser;
import io.welldev.model.service.AppUserService;
import io.welldev.model.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = API.USERS, headers = Strings.HEADERS_JSON, produces = Strings.PRODUCES_JSON)
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    private final MovieService movieService;

    private final ModelMapper mapper;

    @GetMapping     // show all users
    public ResponseEntity<List<AppUserOutput>> showAllUsers() {
        return ResponseEntity.ok(appUserService.showAllUsers());
    }

    @GetMapping(value = API.SHOW_A_USER)
    public ResponseEntity<AppUserOutput> showAUser(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(appUserService.showAUser(username));
    }

    @PostMapping    // user sign up
    public ResponseEntity<AppUserOutput> addUser(@Valid @RequestBody AppUserInput appUserInput) {

        AppUser appUser = new AppUser();
        mapper.map(appUserInput, appUser);

        try {
            appUserService.save(appUser, Strings.USER);
            AppUser createdAppUser = appUserService.findAppUserByUsername(appUser.getUsername());
            AppUserOutput appUserOutput = new AppUserOutput();
            mapper.map(createdAppUser, appUserOutput);

            return new ResponseEntity<>(appUserOutput, HttpStatus.CREATED);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username was not saved Properly");
        } catch (IllegalArgumentException argumentException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
    }

    @PutMapping(value = API.UPDATE_USER_WATCHLIST)
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

}
