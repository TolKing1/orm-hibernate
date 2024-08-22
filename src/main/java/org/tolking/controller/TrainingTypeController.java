package org.tolking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.tolking.dto.trainingType.TrainingTypeDTO;
import org.tolking.service.TrainingTypeService;

import java.util.List;

@RestController
@RequestMapping("/training-type")
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get training type list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of training type"),

    })
    List<TrainingTypeDTO> getAll() {
        return trainingTypeService.getAll();
    }
}
