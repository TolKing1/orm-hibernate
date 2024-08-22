package org.tolking.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.dto.LoginDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for trainee update")
public class TraineeUpdateRequest {
    private LoginDTO login;
    private TraineeUpdateDTO dto;
}

