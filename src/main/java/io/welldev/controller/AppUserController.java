package io.welldev.controller;

import io.welldev.model.constants.Constants.API;
import io.welldev.model.constants.Constants.AppStrings;
import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.AppUserUpdateInput;
import io.welldev.model.datainputobject.ChangePasswordInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.AppUser;
import io.welldev.model.entity.Movie;
import io.welldev.model.service.AppUserService;
import io.welldev.model.service.BlackListingService;
import io.welldev.model.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = API.USERS)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AppUserController {
    private final AppUserService appUserService;

    private final MovieService movieService;

    private final ModelMapper mapper;

    private final BlackListingService blackListingService;

    @GetMapping     // show all users
    public ResponseEntity<List<AppUserOutput>> showAllUsers() throws IOException{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appUserService.showAllUsers());
    }

    @GetMapping(value = API.SHOW_A_USER)
    public ResponseEntity<AppUserOutput> showAUser(@PathVariable("username") String username) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appUserService.showAUser(username));
    }

    @PutMapping(value = API.UPDATE_USER_WATCHLIST)
    public ResponseEntity<AppUserOutput> addMovieToWatchList(@PathVariable("username") String reqUsername,
                                                             @RequestBody List<UserMovieInput> userMovieInputs)
    throws IOException {
        return ResponseEntity
                .ok()
                .body(appUserService.updateWatchlist(reqUsername, userMovieInputs));
    }

    @DeleteMapping(value = API.DELETE_FROM_USER_WATCHLIST)
    public ResponseEntity<AppUserOutput> deleteMovieFromWatchList(@PathVariable("username") String reqUsername,
                                                                  @RequestBody List<UserMovieInput> userMovieInputs)
    throws IOException {
        return ResponseEntity
                .ok()
                .body(appUserService.deleteFromWatchlist(reqUsername, userMovieInputs));
    }

    @PutMapping (API.UPDATE_A_USER)    // update user info
    public ResponseEntity<AppUserOutput> editUser(@PathVariable("username") String reqUsername,
                                                  @Valid @RequestBody AppUserUpdateInput appUserUpdateInput)
    throws IOException {
        return ResponseEntity
                .ok()
                .body(appUserService.updateUserInfo(reqUsername, appUserUpdateInput));
    }

    @PostMapping    // user sign up
    public ResponseEntity<AppUserOutput> addUser(@Valid @RequestBody AppUserInput appUserInput) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appUserService.userSignUp(appUserInput));
    }

    @PutMapping(API.UPDATE_USER_IMAGE)
    public ResponseEntity<AppUserOutput> updateUserImage(@PathVariable String username,
                                                   @RequestParam("image") MultipartFile imgFile) throws IOException {
//        StringBuilder fileNames = new StringBuilder();
//        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imgFile.getOriginalFilename());
//        Files.write(fileNameAndPath, imgFile.getBytes());
//        MovieInput movieInput = new MovieInput();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appUserService.updateUserImage(username, imgFile));
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

    @PatchMapping(API.CHANGE_USER_PASSWORD)
    public ResponseEntity<Object> changePassword(@PathVariable String username,
                                                 @Valid @RequestBody ChangePasswordInput changePasswordInput) {
        appUserService.changePassword(username, changePasswordInput);
        return ResponseEntity
                .ok()
                .build();
    }

}
