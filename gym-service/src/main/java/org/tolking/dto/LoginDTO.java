package org.tolking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotBlank(message = "Login username can't be empty")
    private String username;

    @Size(min = 3, max = 100, message = "Password length should be between 3 and 100")
    private String password;
}
