package org.tolking.training_event_service.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.tolking.training_event_service.dto.TrainingEventDTO;
import org.tolking.training_event_service.model.TrainerSummary;
import org.tolking.training_event_service.service.TrainingEventService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class TrainingEventController {
    private static final Logger log = LoggerFactory.getLogger(TrainingEventController.class);
    private final TrainingEventService trainingEventService;

    @Operation(summary = "Create a new training event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training event created successfully"),
            @ApiResponse(responseCode = "403", description = "Bad request", content = @Content)
    })
    @PostMapping
    public void createEvent(@RequestBody TrainingEventDTO trainingEventDTO) {
        trainingEventService.create(trainingEventDTO);
    }

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
