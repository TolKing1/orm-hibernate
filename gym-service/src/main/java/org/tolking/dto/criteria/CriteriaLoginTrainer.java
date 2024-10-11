package org.tolking.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.dto.LoginDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaLoginTrainer {
    LoginDTO loginDTO;
    CriteriaTrainerDTO criteria;
}
