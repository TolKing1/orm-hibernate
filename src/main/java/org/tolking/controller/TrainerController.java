package org.tolking.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
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
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.service.TrainerService;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import static org.tolking.util.ControllerUtils.*;


@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get trainer profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
    })
    TrainerProfileDTO profile(@NotNull Principal principal) {

        return trainerService.getProfile(principal.getName());
    }

    @PutMapping("/profile/changePassword")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer profile change password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = PASSWORD_HAS_BEEN_CHANGED)

    })
    void changePassword(@NotNull Principal principal,
                        @RequestBody @Valid NewPassword newPassword,
                        BindingResult result) {
        throwExceptionIfHasError(result);

        trainerService.updatePassword(principal.getName(), newPassword.getPassword());
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Timed(value = "trainer_profile.time", description = "Time taken to update trainer's profile")
    @Operation(summary = "Trainer profile change profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = PROFILE_HAS_BEEN_CHANGED)

    })
    TrainerProfileDTO update(@NotNull Principal principal,
                             @RequestBody @Valid TrainerUpdateDTO updateDto,
                             BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return trainerService.update(principal.getName(), updateDto);
    }

    @PatchMapping("/profile/toggleStatus")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer profile toggle status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = STATUS_HAS_BEEN_TOGGLED)

    })
    void toggleStatus(@NotNull Principal principal) {
        trainerService.toggleStatus(principal.getName());
    }

    @GetMapping("/training/criteria")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Trainer's training list by criteria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = TRAINING_RETRIEVED)

    })
    List<TrainingTrainerReadDTO> getTrainingList(@NotNull Principal principal,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodFrom,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodTo,
                                                 @RequestParam(required = false) String traineeName) {
        return trainerService.getTrainingList(
                principal.getName(),
                CriteriaTrainerDTO.builder()
                        .from(periodFrom)
                        .to(periodTo)
                        .traineeUsername(traineeName)
                        .build()
        );
    }
}
