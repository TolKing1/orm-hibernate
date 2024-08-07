package org.tolking.dto.trainer;

import lombok.*;
import org.tolking.dto.user.UserProfileDTO;
import org.tolking.dto.trainingType.TrainingTypeNameDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDTO {
    @NonNull
    private TrainingTypeNameDTO trainingType;
    private UserProfileDTO user;
}
