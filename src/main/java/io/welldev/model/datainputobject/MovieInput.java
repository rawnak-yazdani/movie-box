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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieInput {

    @NotBlank(message = "Movie title cannot be blank!")
    @Pattern(regexp = "^[a-zA-Z0-9-\\s]+$",
            message = "Only alphabetical characters and numerical values are allowed for movie title")
    private String title;

//    @NotBlank(message = "Image Required")
//    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)",
//            message = "Invalid URL")
//    private String imgSrc;

    @NotBlank(message = "Movie description cannot be blank!")
//    @Pattern(regexp = "^[a-zA-Z0-9-\\s,!.?'/]+$",
//            message = "Description is not descriptive enough")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "10.0")
    @Digits(integer = 2, fraction = 1)
    private BigDecimal rating;

    @Valid
    private Set<GenreInput> genres = new HashSet<GenreInput>();

    @Min(1000)
    @Max(2030)
    private Integer year;
}
