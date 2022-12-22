package io.welldev.model.datainputobject;

import io.welldev.model.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieInput {

    private String title;

    private String rating;

    private Set<Genre> genres = new HashSet<Genre>();

    private int year;
}
