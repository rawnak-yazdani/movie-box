package io.welldev.model.datainputobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenreInput {

    @NotBlank(message = "Genre name cannot be blank!")
    @Pattern(regexp = "^[a-zA-Z\\-]+$", message = "Only alphabetical characters and hyphen are allowed for genre name")
    private String name;
}
