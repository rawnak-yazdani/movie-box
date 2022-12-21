package io.welldev.controller;

import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import io.welldev.model.constants.Constants.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = API.CONTEXT_PATH, headers = Strings.HEADERS_JSON, produces = Strings.PRODUCES_JSON)
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;

    private final GenreService genreService;

    private final AppUserService appUserService;

    private final ModelMapper mapper;

    @GetMapping(value = API.SHOW_ALL_MOVIES)
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.FOUND);
    }

//    @PutMapping("/logout")
//    public ResponseEntity<Void> logout() {
//        blackListingService.blackListJwt(userRequestScopeBean.getJwt());
//        return ResponseEntity.ok(null);
//    }

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

}
