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
import org.springframework.security.core.context.SecurityContextHolder;
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
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(movieService.findAll());
    }

}
