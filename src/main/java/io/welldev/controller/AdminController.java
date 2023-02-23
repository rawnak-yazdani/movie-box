package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.MovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.dataoutputobject.MovieOutput;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import io.welldev.model.constants.Constants.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = API.ADMIN)
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;

    private final GenreService genreService;

    private final AppUserService appUserService;

    @PostMapping    // other admin signup
    public ResponseEntity<AppUserOutput> addOtherAdmin(@Valid @RequestBody AppUserInput appUserInput)
            throws IOException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appUserService.adminSignUp(appUserInput));
    }

    @PostMapping(value = API.ADD_A_USER_BY_ADMIN)
    public ResponseEntity<AppUserOutput> addUser(@Valid @RequestBody AppUserInput appUserInput) throws IOException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appUserService.userSignUp(appUserInput));
    }

    @PostMapping(value = API.ADD_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Movie> addMovie(@RequestParam("userData") String movieInputString,
                                          @RequestParam("image") MultipartFile imgFile) throws IOException {
//        StringBuilder fileNames = new StringBuilder();
//        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imgFile.getOriginalFilename());
//        Files.write(fileNameAndPath, imgFile.getBytes());
//        MovieInput movieInput = new MovieInput();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movieService.addMovie(movieInputString, imgFile));
    }

    @PatchMapping(API.UPDATE_A_MOVIE_BY_ADMIN)
    public ResponseEntity<MovieOutput> updateMovie(@PathVariable("id") Long id,
                                                   @RequestBody MovieInput movieInput) throws IOException {
        return ResponseEntity
                .ok()
                .body(movieService.updateAMovieInfo(id, movieInput));
    }

    @PatchMapping(API.UPDATE_A_MOVIE_IMAGE_BY_ADMIN)
    public ResponseEntity<MovieOutput> updateMovieImage(@PathVariable("id") Long id,
                                                        @RequestParam("image") MultipartFile imgFile)
            throws IOException {
        return ResponseEntity
                .ok()
                .body(movieService.updateMovieImage(id, imgFile));
    }

    @DeleteMapping(API.DELETE_A_MOVIE_BY_ADMIN)
    public ResponseEntity<Object> deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(API.GET_ALL_GENRES)
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(genreService.findAll());
    }
}
