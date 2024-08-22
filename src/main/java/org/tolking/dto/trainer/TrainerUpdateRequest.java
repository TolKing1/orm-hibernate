package org.tolking.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.dto.LoginDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateRequest {
    private LoginDTO login;
    private TrainerUpdateDTO dto;
}
