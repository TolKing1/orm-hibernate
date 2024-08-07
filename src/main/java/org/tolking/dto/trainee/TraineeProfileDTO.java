package org.tolking.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.dto.user.UserProfileDTO;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileDTO {
    private Date dateOfBirth;
    private String address;
    private UserProfileDTO user;
}
