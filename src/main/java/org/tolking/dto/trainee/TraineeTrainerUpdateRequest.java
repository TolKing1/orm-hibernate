package org.tolking.dto.trainee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainer.TrainerNameDTO;

import java.util.List;

import static org.tolking.dto.ValidationConstant.LOGIN_CAN_T_BE_NULL;

@Data
public class TraineeTrainerUpdateRequest {
    @NotNull(message = LOGIN_CAN_T_BE_NULL)
    @Valid
    private LoginDTO loginDTO;

    @NotNull(message = "Trainer list can't be empty")
    @Valid
    private List<TrainerNameDTO> dtoList;
}
