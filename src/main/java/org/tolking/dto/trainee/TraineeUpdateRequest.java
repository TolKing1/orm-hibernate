package org.tolking.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.dto.LoginDTO;

import static org.tolking.dto.ValidationConstant.LOGIN_CAN_T_BE_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for trainee update")
public class TraineeUpdateRequest {
    @NotNull(message = LOGIN_CAN_T_BE_NULL)
    @Valid
    private LoginDTO login;

    @NotNull(message = "Trainee can't be null")
    @Valid
    private TraineeUpdateDTO dto;
}

