package org.tolking.external_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.tolking.enums.ActionType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class TrainingEventDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @JsonProperty("actionType")
    private ActionType actionType;
}
