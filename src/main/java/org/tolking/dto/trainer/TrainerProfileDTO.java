package org.tolking.dto.trainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.dto.trainee.TraineeForTrainerProfileDTO;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDTO {
    @JsonProperty("firstName")
    private String userFirstName;
    @JsonProperty("lastName")
    private String userLastName;
    @JsonProperty("specialization")
    private String trainingTypeName;
    private boolean userIsActive;
    private List<TraineeForTrainerProfileDTO> traineeList;
}
