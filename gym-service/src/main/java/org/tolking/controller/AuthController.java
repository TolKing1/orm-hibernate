package org.tolking.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainee.TraineeCreateDTO;
import org.tolking.dto.trainer.TrainerCreateDTO;
import org.tolking.exception.ApiError;
import org.tolking.service.TraineeService;
import org.tolking.service.TrainerService;
import org.tolking.service.UserDetailsService;

import static org.tolking.config.CustomMetricsConfig.CREATED_TRAINEE_COUNT;
import static org.tolking.util.ControllerUtils.ERROR_IN_VALIDATION;
import static org.tolking.util.ControllerUtils.throwExceptionIfHasError;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserDetailsService userDetailsService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login", description = "Logs in the user by verifying the credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
    })
    public String login(@RequestBody LoginDTO loginDTO) {
        return userDetailsService.signIn(loginDTO);
    }

    private final MeterRegistry meterRegistry;

    @PostMapping("/trainee/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Trainee", description = "Creates a new Trainee and returns login details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(schema = @Schema(implementation = LoginDTO.class))),
            @ApiResponse(responseCode = "400", description = ERROR_IN_VALIDATION,
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
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

    @PostMapping("/trainer/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create trainer", description = "Create operation for trainer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainer is created successfully"),
            @ApiResponse(responseCode = "400",
                    description = ERROR_IN_VALIDATION,
                    content = @Content( schema = @Schema(implementation = ApiError.class))),

    })
    LoginDTO create(@RequestBody @Valid TrainerCreateDTO dto,
                    BindingResult bindingResult) {
        throwExceptionIfHasError(bindingResult);

        return trainerService.create(dto);
    }
}
