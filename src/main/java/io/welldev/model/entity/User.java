package io.welldev.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @ManyToMany
    @JoinTable(name = "movie_user",
            joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_movie")})
    private Set<Movie> watchList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Movie> getWatchList() {
        return watchList;
    }

    public void setWatchList(Set<Movie> watchList) {
        this.watchList = watchList;
    }
}
