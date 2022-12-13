package io.welldev.controller;

import com.sun.xml.txw2.IllegalAnnotationException;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // user sign up
    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cinephile> addUser(@Valid @RequestBody Credentials credentials) {
        try {
            credentialsService.save(credentials, "user");
            Cinephile createdCinephile = credentialsService.findCredentialsByUsername(credentials.getUsername()).getCinephile();

            return new ResponseEntity<>(createdCinephile, HttpStatus.CREATED);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username was not saved Properly");
        } catch (IllegalArgumentException argumentException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
    }
/*
{
    "username": "anik1",
    "password": "2222",
    "cinephile": {"name": "Khaleed Ahmed Anik"}
}

{
    "username": "abir2",
    "password": "2222",
    "cinephile": {"name": "Mahadee Hasan Abir"}
}

{
    "username": "arnob3",
    "password": "2222",
    "cinephile": {"name": "Tanveer Arnob"}
}

{
    "username": "rawnak4",
    "password": "2222",
    "cinephile": {"name": "Rawnak Yazdani"}
}
*/
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // main admin sign up
//    @PostMapping(value = "/signup/main-admin")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<Admin> addAdmin(@Valid @RequestBody Credentials credentials) {
//        credentialsService.save(credentials, credentials.getRole());
//        Admin createdAdmin = credentialsService.findCredentialsByUsername(credentials.getUsername()).getAdmin();
//
//        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
//    }
/*
{
    "username": "dictator",
    "password": "2222",
    "role": "admin",
    "admin": {"name": "Mr Dictator"}
}

Token: (from http://localhost:8080/login, with username and password)
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaWN0YXRvciIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJBZG1pbiA6OiBSZWFkIn0seyJhdXRob3JpdHkiOiJBZG1pbiA6OiBXcml0ZSJ9LHsiYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9LHsiYXV0aG9yaXR5IjoiVXNlciA6OiBSZWFkIn0seyJhdXRob3JpdHkiOiJVc2VyIDo6IFdyaXRlIn1dLCJpYXQiOjE2NzA5MDcxOTgsImV4cCI6MTY3MTA0MDgwMH0.V3Uqm5CGJoxOOqTzdqnYpSTYFKErXt6W1gvEZmdqeCiZrWkkO0g_8hfSCwwrsBrxu2YK59OO2DJaFfMdonKrpg
*/
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



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
