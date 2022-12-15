package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/", headers = {"Accept=application/json"},
        produces = "application/json")
public class HomeController {

    @Autowired
    MovieService movieService;
//    List<Movie> movies = movieService.findAll();

    @Autowired
    GenreService genreService;

    @Autowired
    AppUserService appUserService;

    List<DemoPurpose> movies = new ArrayList<>(Arrays.asList(
            new DemoPurpose(1, "Movie 1"),
            new DemoPurpose(2, "Movie 2")
    ));

    // user sign up
    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AppUserOutput> addUser(@Valid @RequestBody AppUserInput appUserInput) {

        AppUser appUser = new AppUser();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(appUserInput, appUser);

        try {
            appUserService.save(appUser, "user");
            AppUser createdAppUser = appUserService.findAppUserByUsername(appUser.getUsername());
            AppUserOutput appUserOutput = new AppUserOutput();
            modelMapper.map(createdAppUser, appUserOutput);

            return new ResponseEntity<>(appUserOutput, HttpStatus.CREATED);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username was not saved Properly");
        } catch (IllegalArgumentException argumentException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
    }

//    // main admin sign up
//    @PostMapping(value = "/signup/main-admin")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<AppUser> addMainAdmin(@Valid @RequestBody AppUser credentials) {
//        System.out.printf(credentials.getName() + credentials.getId());
//        appUserService.save(credentials, "admin");
//        AppUser createdAdmin = appUserService.findAppUserByUsername(credentials.getUsername());
//
//        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
//    }

    // show all movies
    @GetMapping(value = "/movies")
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.FOUND);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
{
    "name": "Khaleed Ahmed Anik",
    "username": "anik1",
    "password": "2222"
}

{
    "name": "Mahadee Hasan Abir",
    "username": "abir2",
    "password": "2222"
}

{
    "name": "Tanveer Arnob",
    "username": "arnob3",
    "password": "2222"
}

{
    "name": "Rawnak Yazdani",
    "username": "rawnak4",
    "password": "2222"
}

{
    "name": "Mr Dictator",
    "username": "dictator",
    "password": "2222"
}
*/

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }

}
