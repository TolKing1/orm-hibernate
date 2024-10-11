package org.tolking.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPassword {
    /**
     * (?=.*\d)          // should contain at least one digit
     * (?=.*[a-z])       // should contain at least one lower case
     * (?=.*[A-Z])       // should contain at least one upper case
     * [a-zA-Z0-9]{8,}   // should contain at least 8 from the mentioned characters
     */
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$",
            message = "Password must contain at least 1 number, 1 uppercase and 1 lowercase letters, at least 8 characters")
    private String password;
}
