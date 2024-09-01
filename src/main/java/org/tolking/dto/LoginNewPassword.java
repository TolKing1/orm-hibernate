package org.tolking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.tolking.dto.ValidationConstant.LOGIN_CAN_T_BE_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginNewPassword {
    @NotNull(message = LOGIN_CAN_T_BE_NULL)
    @Valid
    private LoginDTO loginDTO;

    @Size(min = 3, max = 100, message = "New password length should be between 3 and 100")
    private String newPassword;
}
