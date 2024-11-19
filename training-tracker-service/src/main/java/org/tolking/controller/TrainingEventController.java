package org.tolking.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tolking.model.TrainerSummary;
import org.tolking.service.TrainingEventService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
@Slf4j
public class TrainingEventController {
    private final TrainingEventService trainingEventService;

    @Operation(summary = "Get summary of all trainers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerSummary.class))
            })
    })
    @GetMapping("/summary")
    @CircuitBreaker(name = "event-service", fallbackMethod = "breakerMessage")
    public List<TrainerSummary> getSummary() {
        return trainingEventService.getAllSummary();
    }

    public List<TrainerSummary> breakerMessage(Throwable throwable) {
        log.debug("Breaker message from getSummary()");
        return new ArrayList<>();
    }
}
