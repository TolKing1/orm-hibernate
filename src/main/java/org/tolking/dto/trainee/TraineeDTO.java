package org.tolking.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.dto.user.UserCreateDTO;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDTO {
    private Date dateOfBirth;
    private String address;
    private UserCreateDTO user;
}
