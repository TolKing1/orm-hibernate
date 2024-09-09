package org.tolking.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tolking.dto.NewPassword;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainee.TraineeUpdateDTO;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.trainer.TrainerNameDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.ApiError;
import org.tolking.service.TraineeService;
import org.tolking.service.TrainingService;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import static org.tolking.util.ControllerUtils.ERROR_IN_VALIDATION;
import static org.tolking.util.ControllerUtils.throwExceptionIfHasError;


@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {
    public static final String CONTENT_TYPE = "application/json";

    private final TraineeService traineeService;
    private final TrainingService trainingService;


    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Trainee Profile", description = "Retrieves the profile details of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TraineeProfileDTO.class))),
    })
    public TraineeProfileDTO profile(@NotNull Principal principal) {
        return traineeService.getProfile(principal.getName());
    }

    @PutMapping("/profile/changePassword")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change Password", description = "Allows the trainee to change their password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
    })
    public void changePassword(@NotNull Principal principal,
                               @RequestBody @Valid NewPassword newPassword,
                               BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        traineeService.updatePassword(principal.getName(), newPassword.getPassword());
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Profile", description = "Updates the profile information of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TraineeProfileDTO.class))),
    })
    public TraineeProfileDTO update(@NotNull Principal principal,
                                    @RequestBody @Valid TraineeUpdateDTO dto,
                                    BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return traineeService.update(principal.getName(), dto);
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete Profile", description = "Deletes the profile of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
    })
    public void deleteProfile(@NotNull Principal principal) {

        traineeService.delete(principal.getName());
    }

    @PatchMapping("/profile/toggleStatus")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Toggle Status", description = "Toggles the active status of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status toggled successfully"),
    })
    public void toggleStatus(@NotNull Principal principal) {
        traineeService.toggleStatus(principal.getName());
    }

    @GetMapping("/training/criteria")
    @ResponseStatus(HttpStatus.OK)
    @Timed(value = "trainee_training.time", description = "Time taken to return training for trainee")
    @Operation(summary = "Get Training List", description = "Retrieves a list of trainings based on criteria.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Training list retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainingTraineeReadDTO.class))),
    })
    public List<TrainingTraineeReadDTO> getTrainingList(@NotNull Principal principal,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodFrom,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodTo,
                                                        @RequestParam(required = false) String trainerName,
                                                        @RequestParam(required = false) TrainingsType trainingType) {
        return traineeService.getTrainingList(
                        principal.getName(),
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
    })
    public void addTraining(@NotNull Principal principal,
                            @RequestBody @Valid TrainingDTO trainingDTO,
                            BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        Trainee trainee = traineeService.getTraineeByUsername(principal.getName());
        trainingService.createTraining(trainee, trainingDTO);
    }

    @GetMapping("/trainer/not-assigned")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Not Assigned Trainers", description = "Retrieves a list of trainers that are not assigned to the trainee and have an active status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer list retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainerForTraineeProfileDTO.class))),
    })
    public List<TrainerForTraineeProfileDTO> getNotAssignedTrainerList(@NotNull Principal principal) {
        return traineeService.getNotAssignedTrainers(principal.getName());
    }

    @PutMapping("/trainer")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Trainer List", description = "Updates the list of trainers assigned to the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer list updated successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainerForTraineeProfileDTO.class))),
    })
    public List<TrainerForTraineeProfileDTO> updateTrainerList(@NotNull Principal principal,
                                                               @RequestBody @Valid List<TrainerNameDTO> dtoList,
                                                               BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return traineeService.updateTrainerList(principal.getName(), dtoList);
    }
}
