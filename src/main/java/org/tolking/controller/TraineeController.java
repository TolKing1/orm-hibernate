package org.tolking.controller;

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
import org.tolking.dto.trainee.TraineeCreateDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainee.TraineeUpdateRequest;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.trainer.TrainerNameDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.ApiError;
import org.tolking.service.TraineeService;
import org.tolking.service.TrainingService;

import java.util.Date;
import java.util.List;

import static org.tolking.util.ControllerUtils.*;


@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {
    public static final String CONTENT_TYPE = "application/json";

    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Trainee", description = "Creates a new Trainee and returns login details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = LoginDTO.class))),
            @ApiResponse(responseCode = "400", description = ERROR_IN_VALIDATION,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public LoginDTO create(@RequestBody @Valid TraineeCreateDTO dto, BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);
        return traineeService.create(dto);
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Trainee Profile", description = "Retrieves the profile details of the trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TraineeProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public TraineeProfileDTO profile(@RequestParam String username, @RequestParam String password) {
        return traineeService.getProfile(new LoginDTO(username, password));
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainee Login", description = "Logs in the trainee by verifying the credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public void login(@RequestParam String username, @RequestParam String password) {
        traineeService.getProfile(new LoginDTO(username, password));
    }

    @PutMapping("/profile/changePassword")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change Password", description = "Allows the trainee to change their password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public void changePassword(@RequestBody LoginNewPassword login) {
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
    public TraineeProfileDTO update(@RequestBody TraineeUpdateRequest updateRequest) {
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
    public void deleteProfile(@RequestBody LoginDTO dto) {
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
    public void toggleStatus(@RequestBody LoginDTO login) {
        traineeService.toggleStatus(login);
    }

    @GetMapping("/training")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Training List", description = "Retrieves a list of trainings based on criteria.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Training list retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainingTraineeReadDTO.class))),
            @ApiResponse(responseCode = "401", description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class)))
    })
    public List<TrainingTraineeReadDTO> getTrainingList(@RequestParam String username,
                                                        @RequestParam String password,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodFrom,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodTo,
                                                        @RequestParam(required = false) String trainerName,
                                                        @RequestParam(required = false) TrainingsType trainingType) {
        return traineeService.getTrainingList(
                new LoginDTO(username, password),
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
    public void addTraining(@RequestParam String username,
                            @RequestParam String password,
                            @RequestBody @Valid TrainingDTO trainingDTO,
                            BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        Trainee trainee = traineeService.getTraineeByLogin(new LoginDTO(username, password));
        trainingService.createTraining(trainee, trainingDTO);
    }

    @GetMapping("/trainer/not-assigned")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Not Assigned Trainers", description = "Retrieves a list of trainers that are not assigned to the trainee and have an active status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer list retrieved successfully",
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = TrainerForTraineeProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect credentials",
                    content = @Content(mediaType = CREDENTIALS_IS_INCORRECT, schema = @Schema(implementation = ApiError.class)))
    })
    public List<TrainerForTraineeProfileDTO> getNotAssignedTrainerList(@RequestParam String username,
                                                                       @RequestParam String password) {
        return traineeService.getNotAssignedTrainers(new LoginDTO(username, password));
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
    public List<TrainerForTraineeProfileDTO> updateTrainerList(@RequestParam String username,
                                                               @RequestParam String password,
                                                               @RequestBody List<TrainerNameDTO> dtoList) {
        return traineeService.updateTrainerList(new LoginDTO(username, password), dtoList);
    }
}
