package org.tolking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginNewPassword {
    private String username;
    private String password;
    private String newPassword;
}
