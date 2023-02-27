package io.welldev.model.datainputobject;

import io.welldev.model.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppUserInput {

    @NotBlank(message = "Name cannot be blank!")
    @Pattern(regexp = "^[a-zA-Z-\\s]+$", message = "Only alphabetical characters are allowed for name")
    private String name;

    @NotBlank(message = "Username cannot be blank!")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "Username length should be minimum 3 characters and maximum 10 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank!")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*%^])(?=.*[0-9])(?=.*[a-z]).{8,20}$",
            message = "Password must contain at least one capital letter, a number, a special character "
            + "and have minimum length of 8")
    private String password;

}
