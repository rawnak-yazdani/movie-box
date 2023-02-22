package io.welldev.model.datainputobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppUserUpdateInput {
    @Pattern(regexp = "^[a-zA-Z-\\s]+$", message = "Only alphabetical characters are allowed for name")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "Username length should be minimum 3 characters and maximum 10 characters")
    private String username;

}
