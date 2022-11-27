package io.welldev.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = {@JoinColumn(name = "fk_movie")},
            inverseJoinColumns = {@JoinColumn(name = "fk_genre")})
    @Fetch(value = FetchMode.SELECT)
    private Set<Genre> genres = new HashSet<Genre>();
    private String rating;
    @ManyToOne
    @JoinColumn(name = "fk_director")
    private Director director;

    @ManyToMany
    @JoinTable(name = "movie_actors",
            joinColumns = {@JoinColumn(name = "fk_movie")},
            inverseJoinColumns = {@JoinColumn(name = "fk_actor")})
    @Fetch(value = FetchMode.JOIN)
    private Set<Actor> actors = new HashSet<Actor>();

    private int year;

    @OneToMany
    @JoinColumn(name = "fk_movie")
    private List<Review> reviewList;

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Movie() {
    }

    public Movie(String title, Set<Genre> genres, String rating, Director director, Set<Actor> actors, int year, List<Review> reviewList) {
        this.title = title;
        this.genres = genres;
        this.rating = rating;
        this.director = director;
        this.actors = actors;
        this.year = year;
        this.reviewList = reviewList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
}
