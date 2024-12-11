package org.tolking.integration.controller.steps.Training;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tolking.entity.Trainee;
import org.tolking.entity.Training;
import org.tolking.integration.config.TestContext;
import org.tolking.repository.TraineeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tolking.integration.util.GlobalConstants.AUTHORIZATION_HEADER;
import static org.tolking.integration.util.GlobalConstants.CONTENT_TYPE_JSON;

@RequiredArgsConstructor
public class CancelSteps {
    private final TestContext testContext;
    private final MockMvc mockMvc;
    private final TraineeRepository traineeRepository;
    private final String TRAINEE_TRAINING_LIST_URL = "/trainee/training";
    private Long trainingId;
    private MvcResult mvcResult;

    @Given("the training list are assigned to trainee {string} with trainingId")
    public void theTrainingListAreAssignedToTraineeWithTrainingId(String username) {
        Trainee trainee = traineeRepository.getTraineeByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));
        Optional<Training> trainingOptional = trainee.getTrainingList().stream()
                .findFirst();

        assertTrue(trainingOptional.isPresent());
        trainingOptional.ifPresent(training -> {
            assertFalse(training.isDeleted());
            trainingId = training.getId();
        });
    }

    private Optional<Training> getOptionalTraining(String username) {
        Trainee trainee = traineeRepository.getTraineeByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        return trainee.getTrainingList().stream()
                .filter(training -> training.getId().equals(trainingId))
                .findFirst();
    }

    @When("the trainee requests to cancel training with trainingId")
    public void theTraineeRequestsToCancelTrainingWithTrainingId() throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(TRAINEE_TRAINING_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(trainingId.toString()))
                .andReturn();
    }

    @When("the trainee requests to cancel training with invalid trainingId")
    public void theTraineeRequestsToCancelTrainingWithInvalidTrainingId() throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(TRAINEE_TRAINING_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON)
                        .content("213982132"))
                .andReturn();
    }

    @Then("the response status for training should be {int}")
    public void theResponseStatusForTrainingShouldBe(int status) {
        assertEquals(status, mvcResult.getResponse().getStatus());
    }

    @And("the training for {string} should be cancelled")
    public void theTrainingShouldBeCancelled(String username) {
        Optional<Training> trainingWithId = getOptionalTraining(username);
        assertTrue(trainingWithId.isPresent());
        trainingWithId.ifPresent(training -> assertTrue(training.isDeleted()));
    }
}
