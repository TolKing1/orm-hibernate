package org.tolking.dto.trainer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TrainerNameDTO {
    @NotEmpty(message = "Trainer username can't be empty")
    private String username;
}
