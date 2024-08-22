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
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.trainer.TrainerCreateDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateRequest;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.exception.ApiError;
import org.tolking.service.TrainerService;

import java.util.Date;
import java.util.List;

import static org.tolking.util.ControllerUtils.*;


@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    public static final String CONTENT_TYPE = "application/json";
    private final TrainerService trainerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create trainer", description = "Create operation for trainer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainer is created successfully"),
            @ApiResponse(responseCode = "400",
                    description = ERROR_IN_VALIDATION,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),

    })
    LoginDTO create(@RequestBody @Valid TrainerCreateDTO dto,
                    BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return trainerService.create(dto);
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get trainer profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401",
                    description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),

    })
    TrainerProfileDTO profile(@RequestParam String username, @RequestParam String password) {
        return trainerService.getProfile(new LoginDTO(username, password));
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401",
                    description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),

    })
    void login(@RequestParam String username, @RequestParam String password) {
        trainerService.getProfile(new LoginDTO(username, password));
    }

    @PutMapping("/profile/changePassword")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer profile change password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = PASSWORD_HAS_BEEN_CHANGED),
            @ApiResponse(responseCode = "401",
                    description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),

    })
    void changePassword(@RequestBody LoginNewPassword login) {
        trainerService.updatePassword(login);
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer profile change profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = PROFILE_HAS_BEEN_CHANGED),
            @ApiResponse(responseCode = "401",
                    description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),

    })
    TrainerProfileDTO update(@RequestBody TrainerUpdateRequest updateRequest) {
        return trainerService.update(updateRequest.getLogin(), updateRequest.getDto());
    }

    @PatchMapping("/profile/toggleStatus")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer profile toggle status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = STATUS_HAS_BEEN_TOGGLED),
            @ApiResponse(responseCode = "401",
                    description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),

    })
    void toggleStatus(@RequestBody LoginDTO login) {
        trainerService.toggleStatus(login);
    }

    @GetMapping("/training")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer's training list by criteria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = TRAINING_RETRIEVED),
            @ApiResponse(responseCode = "401",
                    description = CREDENTIALS_IS_INCORRECT,
                    content = @Content(mediaType = CONTENT_TYPE, schema = @Schema(implementation = ApiError.class))),

    })
    List<TrainingTrainerReadDTO> getTrainingList(@RequestParam String username,
                                                 @RequestParam String password,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodFrom,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodTo,
                                                 @RequestParam(required = false) String traineeName) {
        return trainerService.getTrainingList(
                new LoginDTO(username, password),
                CriteriaTrainerDTO.builder()
                        .from(periodFrom)
                        .to(periodTo)
                        .traineeUsername(traineeName)
                        .build()
        );
    }
}
