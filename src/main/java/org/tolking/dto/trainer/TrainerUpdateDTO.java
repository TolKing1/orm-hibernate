package org.tolking.dto.trainer;

import lombok.*;
import org.tolking.dto.trainingType.TrainingTypeIdDTO;
import org.tolking.dto.user.UserProfileDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateDTO {
    @NonNull
    private TrainingTypeIdDTO trainingType;
    private UserProfileDTO user;
}
