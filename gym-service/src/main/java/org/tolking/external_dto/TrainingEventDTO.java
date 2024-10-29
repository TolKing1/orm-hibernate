package org.tolking.external_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.tolking.enums.ActionType;

import java.util.Date;

@Data
public class TrainingEventDTO {
    @JsonProperty("username")
    private String trainerUserUsername;
    @JsonProperty("firstName")
    private String trainerUserFirstName;
    @JsonProperty("lastName")
    private String trainerUserLastName;
    @JsonProperty("isActive")
    private boolean trainerUserIsActive;
    @JsonProperty("trainingId")
    private Long id;
    @JsonProperty("trainingDate")
    private Date date;
    @JsonProperty("trainingDuration")
    private int duration;

    private ActionType actionType;
}
