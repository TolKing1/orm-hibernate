package org.tolking.dto.trainee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.training.TrainingDTO;

import static org.tolking.dto.ValidationConstant.LOGIN_CAN_T_BE_NULL;
import static org.tolking.dto.ValidationConstant.TRAINING_CAN_T_BE_NULL;

@Data
public class TraineeTrainingCreateRequest {
    @NotNull(message = LOGIN_CAN_T_BE_NULL)
    @Valid
    private LoginDTO loginDTO;

    @NotNull(message = TRAINING_CAN_T_BE_NULL)
    @Valid
    private TrainingDTO trainingDTO;
}
