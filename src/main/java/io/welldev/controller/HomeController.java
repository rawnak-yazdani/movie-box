package io.welldev.controller;

import io.welldev.initializer.configuration.authorization.JwtTokenVerifier;
import io.welldev.model.dataoutputobject.MovieOutput;
import io.welldev.model.entity.*;
import io.welldev.model.service.*;
import io.welldev.model.constants.Constants.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = API.CONTEXT_PATH, headers = AppStrings.HEADERS_JSON, produces = AppStrings.PRODUCES_JSON)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {

    private final MovieService movieService;

    private final GenreService genreService;

    private final AppUserService appUserService;

    private final ModelMapper mapper;
    private final JwtTokenVerifier jwtTokenVerifier;

    @GetMapping(value = API.SHOW_ALL_MOVIES)
    public ResponseEntity<List<MovieOutput>> getMovies() throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieService.findAll());
    }

    @GetMapping(value = API.SHOW_A_MOVIE)
    public ResponseEntity<MovieOutput> getMovie(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movieService.findById(id));
    }

    @GetMapping(value = API.AUTH_REFRESH)
    public void getAccessToken(HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        jwtTokenVerifier.refreshToken(request, response);
    }

}
