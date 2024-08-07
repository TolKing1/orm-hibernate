package org.tolking.dto.trainer;

import lombok.*;
import org.tolking.dto.trainingType.TrainingTypeIdDTO;
import org.tolking.dto.user.UserCreateDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    @NonNull
    private TrainingTypeIdDTO trainingType;
    private UserCreateDTO user;
}
