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
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@RequiredArgsConstructor
@Service
@Transactional
public class MovieService {
    private final MovieRepo movieRepo;

    private final GenreRepo genreRepo;

    private final GenreService genreService;

    private final ModelMapper mapper;

    private final ObjectMapper objectMapper;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/app-photos/movies";

    public Movie save(Movie movie) {
        Set<Genre> genres = movie.getGenres();
        Set<Genre> updatedGenre = new HashSet<>();
//        genreService.saveAll(genres);
//        Iterator<Genre> genreIterator = genres.iterator();
        genreService.saveWithoutDuplicate(genres);
//        movie.setGenres(updatedGenre);
//        genreRepo.findByName("Action");
//        return movie;
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

    public MovieOutput updateAMovieInfo(Long id, MovieInput movieInput) throws IOException{
        Movie movie = findMovieById(id);
//        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(movieInput, movie);
//        if (movieInput.getGenres() != null) {
//            movie.setGenres(movieInput.getGenres());
//        }

        return mapMovie(save(movie));
    }

    public MovieOutput updateMovieImage(Long id, MultipartFile imgFile) throws IOException {
        Movie movie = findMovieById(id);
        ImageUtils.deleteFile(movie.getImgSrc());
        movie.setImgSrc(ImageUtils.writeMovieImageFile(movie, imgFile));
        return mapMovie(movie);
    }

    public Movie addMovie(String movieInputString, MultipartFile imgFile) throws IOException {
        Movie movie = new Movie();
        MovieInput movieInput = objectMapper.readValue(movieInputString, MovieInput.class);
        mapper.map(movieInput, movie);
        ImageUtils.writeMovieImageFile(movie, imgFile);
        movie.setImgSrc(ImageUtils.writeMovieImageFile(movie, imgFile));
        movie.setRating(movieInput.getRating().toString().concat("/").concat("10"));
        return save(movie);
    }

    public MovieOutput findById(Long id) {
        try {
            return mapMovie(movieRepo.findById(id).get());
        } catch (Exception ex) {
            throw new ItemNotFoundException("This movie does not exist!");
        }
    }

    public Movie findMovieById(Long id) {
        try {
            return movieRepo.findById(id).get();
        } catch (Exception ex) {
            throw new ItemNotFoundException("This movie does not exist!");
        }
    }

    public List<MovieOutput> findAll() throws IOException {
        List<MovieOutput> allMovies = new LinkedList<>();
        for (Movie m : movieRepo.findAll()) {
            allMovies.add(mapMovie(m));
        }
        return allMovies;
    }

    public void deleteById(Long id) {
        Movie movie = movieRepo.findById(id).get();
        ImageUtils.deleteFile(movie.getImgSrc());
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

    public MovieOutput mapMovie(Movie movie) throws IOException {
        File file = new File(movie.getImgSrc());
        String imgSrcB64 = ImageUtils.encodeImageToBase64(file);
        MovieOutput movieOutput = new MovieOutput();
        mapper.map(movie, movieOutput);
        movieOutput.setImgSrc(imgSrcB64);

        return movieOutput;
    }
}
