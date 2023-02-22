package io.welldev.model.datainputobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordInput {

    @NotBlank(message = "currentPassword cannot be blank!")
    private String currentPassword;

    @NotBlank(message = "newPassword cannot be blank!")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*%^])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8,20}$",
            message = "Password must contain at least one capital letter, a number," +
                    " a special character and have minimum length of 8")
    private String newPassword;
}
