package io.welldev.model.dataoutputobject;

import io.welldev.model.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppUserOutput {
    private String name;

    private String username;

    private Set<Movie> watchlist = new HashSet<>();
}
