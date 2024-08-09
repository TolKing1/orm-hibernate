package org.tolking.dto.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String username;
    private String password;

}
