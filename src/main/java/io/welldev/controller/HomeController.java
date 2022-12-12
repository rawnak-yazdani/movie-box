package io.welldev.controller;

import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    CredentialsService credentialsService;
    @Autowired
    CinephileService cinephileService;
    @Autowired
    AdminService adminService;

    List<DemoPurpose> movies = new ArrayList<>(Arrays.asList(
            new DemoPurpose(1, "Movie 1"),
            new DemoPurpose(2, "Movie 2")
    ));

    @GetMapping
    public List<Movie> getMovies() {

        List<Movie> movies = movieService.findAll();
//        for (Movie movie:
//                movies) {
//            System.out.println(movie.getTitle());
//        }
        return movies;
    }

    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cinephile> addUser(@Valid @RequestBody Credentials credentials) {
            credentialsService.save(credentials, "user");
            Cinephile createdCinephile = credentialsService.getUserByName(credentials.getUsername()).getCinephile();


        return new ResponseEntity<>(createdCinephile, HttpStatus.CREATED);
    }



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

/*
{
    "username": "Anik3",
    "password": "12345",
    "cinephile": {"name": "Khaleed Ahmed Anik"}
}
*/

}
