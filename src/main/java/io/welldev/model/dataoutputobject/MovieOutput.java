package io.welldev.model.dataoutputobject;

import io.welldev.model.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieOutput {

    private Long id;

    private String title;

    private String imgSrc;

    private String description;

    private Set<Genre> genres = new HashSet<Genre>();

    private String rating;

    private Integer year;
}
