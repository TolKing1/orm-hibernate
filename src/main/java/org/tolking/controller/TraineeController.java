package org.tolking.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.LoginNewPassword;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.trainee.*;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.ApiError;
import org.tolking.service.TraineeService;
import org.tolking.service.TrainingService;

import java.util.Date;
import java.util.List;

import static org.tolking.config.CustomMetricsConfig.CREATED_TRAINEE_COUNT;
import static org.tolking.util.ControllerUtils.*;


@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {
    public static final String CONTENT_TYPE = "application/json";

    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final MeterRegistry meterRegistry;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Trainee", description = "Creates a new Trainee and returns login details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = LoginDTO.class))),
            @ApiResponse(responseCode = "400", description = ERROR_IN_VALIDATION,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public LoginDTO create(@RequestBody @Valid TraineeCreateDTO dto,
                           BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        LoginDTO loginDTO = traineeService.create(dto);

        //Metric
        this.meterRegistry
                .counter(CREATED_TRAINEE_COUNT)
                .increment();

        return loginDTO;
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Trainee Profile", description = "Retrieves the profile details of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TraineeProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public TraineeProfileDTO profile(@RequestBody @Valid LoginDTO loginDTO,
                                     BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return traineeService.getProfile(loginDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainee Login", description = "Logs in the trainee by verifying the credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public void login(@RequestBody LoginDTO loginDTO,
                      BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        traineeService.getProfile(loginDTO);
    }

    @PutMapping("/profile/changePassword")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change Password", description = "Allows the trainee to change their password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public void changePassword(@RequestBody @Valid LoginNewPassword login,
                               BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        traineeService.updatePassword(login);
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Profile", description = "Updates the profile information of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TraineeProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public TraineeProfileDTO update(@RequestBody @Valid TraineeUpdateRequest updateRequest,
                                    BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return traineeService.update(updateRequest.getLogin(), updateRequest.getDto());
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete Profile", description = "Deletes the profile of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public void deleteProfile(@RequestBody @Valid LoginDTO dto,
                              BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        traineeService.delete(dto);
    }

    @PatchMapping("/profile/toggleStatus")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Toggle Status", description = "Toggles the active status of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status toggled successfully"),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public void toggleStatus(@RequestBody @Valid LoginDTO login,
                             BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        traineeService.toggleStatus(login);
    }

    @PostMapping("/training/criteria")
    @ResponseStatus(HttpStatus.OK)
    @Timed(value = "trainee_training.time", description = "Time taken to return training for trainee")
    @Operation(summary = "Get Training List", description = "Retrieves a list of trainings based on criteria.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Training list retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainingTraineeReadDTO.class))),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public List<TrainingTraineeReadDTO> getTrainingList(@RequestBody @Valid LoginDTO loginDTO,
                                                        BindingResult bindingResult,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodFrom,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodTo,
                                                        @RequestParam(required = false) String trainerName,
                                                        @RequestParam(required = false) TrainingsType trainingType) {
        throwExceptionIfHasError(bindingResult);

        return traineeService.getTrainingList(
                        loginDTO,
                CriteriaTraineeDTO.builder()
                        .from(periodFrom)
                        .to(periodTo)
                        .trainerUsername(trainerName)
                        .trainingType(trainingType)
                        .build()
        );
    }

    @PostMapping("/training")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add Training", description = "Adds a training to the trainee's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Training added successfully"),
            @ApiResponse(responseCode = "400", description = ERROR_IN_VALIDATION,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public void addTraining(@RequestBody @Valid TraineeTrainingCreateRequest trainingDTO,
                            BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        Trainee trainee = traineeService.getTraineeByLogin(trainingDTO.getLoginDTO());
        trainingService.createTraining(trainee, trainingDTO.getTrainingDTO());
    }

    @PostMapping("/trainer/not-assigned")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Not Assigned Trainers", description = "Retrieves a list of trainers that are not assigned to the trainee and have an active status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer list retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainerForTraineeProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect credentials",
                    content = @Content(mediaType = CREDENTIALS_IS_INCORRECT, schema = @Schema(implementation = ApiError.class)))
    })
    public List<TrainerForTraineeProfileDTO> getNotAssignedTrainerList(@RequestBody @Valid LoginDTO loginDTO,
                                                                       BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return traineeService.getNotAssignedTrainers(loginDTO);
    }

    @PutMapping("/trainer")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Trainer List", description = "Updates the list of trainers assigned to the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer list updated successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainerForTraineeProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public List<TrainerForTraineeProfileDTO> updateTrainerList(@RequestBody @Valid TraineeTrainerUpdateRequest updateRequest,
                                                               BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return traineeService.updateTrainerList(updateRequest.getLoginDTO(), updateRequest.getDtoList());
    }
}
