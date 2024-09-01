package org.tolking.dto.trainer;

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
public class TrainerUpdateRequest {
    @NotNull(message = LOGIN_CAN_T_BE_NULL)
    @Valid
    private LoginDTO login;

    @NotNull(message = "Dto can't be null")
    @Valid
    private TrainerUpdateDTO dto;
}
