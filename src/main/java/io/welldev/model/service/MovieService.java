package io.welldev.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.welldev.model.datainputobject.MovieInput;
import io.welldev.model.dataoutputobject.MovieOutput;
import io.welldev.model.entity.Genre;
import io.welldev.model.entity.Movie;
import io.welldev.model.exception.ItemNotFoundException;
import io.welldev.model.repository.GenreRepo;
import io.welldev.model.repository.MovieRepo;
import io.welldev.model.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
@Transactional
public class MovieService {
    private final MovieRepo movieRepo;

    private final GenreRepo genreRepo;

    private final ModelMapper mapper;

    private final ObjectMapper objectMapper;

    public Movie save(Movie movie) {
        Set<Genre> genres = movie.getGenres();
//        Iterator<Genre> genreIterator = genres.iterator();
        for (Genre genre :
                genres) {
            Genre existingGenre = genreRepo.findByName(genre.getName());
            if (existingGenre != null) {
                genre.setId(existingGenre.getId());
            } else genreRepo.save(genre);
        }
        return movieRepo.save(movie);
    }

    public void flush() {
        movieRepo.flush();
    }

    public void saveAndFlush(Movie movie) {
        movieRepo.saveAndFlush(movie);
    }

    public void saveAll(List<Movie> moviesList) {
        movieRepo.saveAll(moviesList);
    }

    public void saveAllAndFlush(List<Movie> moviesList) {
        movieRepo.saveAllAndFlush(moviesList);
    }

    public Movie updateAMovieInfo(Long id, MovieInput movieInput) {
        Movie movie = new Movie();
        mapper.map(movieInput, movie);
        movie.setId(findById(id).getId());
        movie.setRating(movieInput.getRating().toString().concat("/").concat("10"));
        return save(movie);
    }

    public Movie addMovie(String movieInputString, String imgSrc) throws IOException {
        Movie movie = new Movie();
        MovieInput movieInput = objectMapper.readValue(movieInputString, MovieInput.class);
        mapper.map(movieInput, movie);
        movie.setImgSrc(imgSrc);
        movie.setRating(movieInput.getRating().toString().concat("/").concat("10"));
        return save(movie);
    }

    public Movie findById(Long id) {
        try {
            return movieRepo.findById(id).get();
        } catch (Exception ex) {
            throw new ItemNotFoundException("This movie does not exist!");
        }
    }

    public List<MovieOutput> findAll() throws IOException {
        List<MovieOutput> allMovies = new LinkedList<>();
        for (Movie m : movieRepo.findAll()) {
            File file = new File(m.getImgSrc());
            String imgSrcB64 = ImageUtils.encodeImageToBase64(file);
            MovieOutput movieOutput = new MovieOutput();
            mapper.map(m, movieOutput);
            movieOutput.setImgSrc(imgSrcB64);
            allMovies.add(movieOutput);
        }
        return allMovies;
    }

    public void deleteById(Long id) {
        findById(id);
        movieRepo.deleteUserAndMovieAssociation(id);
        movieRepo.deleteById(id);
    }

    public void delete(Movie movie) {
        movieRepo.delete(movie);
    }

    public void deleteAllById(List<Long> ids) {
        movieRepo.deleteAllById(ids);
    }

    public void deleteAll(List<Movie> movies) {
        movieRepo.deleteAll(movies);
    }

    public void deleteAll() {
        movieRepo.deleteAll();
    }
}
