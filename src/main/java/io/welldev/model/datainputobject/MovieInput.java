package io.welldev.model.datainputobject;

import io.welldev.model.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieInput {

    @NotBlank(message = "Movie name cannot be blank!")
    @Pattern(regexp = "^[a-zA-Z0-9-\\s]+$", message = "Only alphabetical characters and numerical values are allowed for movie title")
    private String title;

    @NotBlank(message = "Rating cannot be blank!")
    @Pattern(regexp = "^[a-zA-Z0-9./]+$", message = "Only alphabetical characters, numerical values, forward slash and period are allowed for rating")
    private String rating;

    @Valid
    private Set<GenreInput> genres = new HashSet<GenreInput>();

    @Min(1000)
    @Max(9999)
    private int year;
}
