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

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z-\\s]+$", message = "Only alphabetical characters are allowed for name")
    private String name;

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "Username length should be minimum 3 characters and maximum 8 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,50}", message = "Password cannot be empty or contain more than 50 characters")
    private String password;

}
