package org.tolking.integration.controller.steps.Training;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.integration.config.TestContext;
import org.tolking.repository.TraineeRepository;
import org.tolking.repository.TrainerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tolking.integration.util.GlobalConstants.AUTHORIZATION_HEADER;
import static org.tolking.integration.util.GlobalConstants.CONTENT_TYPE_JSON;

@RequiredArgsConstructor
public class AddSteps {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TestContext testContext;
    private final MockMvc mockMvc;
    @MockBean
    private final JmsTemplate jmsTemplate;

    private final String TRAINEE_TRAINING_LIST_URL = "/trainee/training";

    private MvcResult mvcResult;


    @Given("the trainer {string} is available in the system")
    public void theTrainerIsAvailableInTheSystem(String trainerUsername) {
        Optional<Trainer> trainerOptional = trainerRepository.getTrainerByUser_Username(trainerUsername);

        assertTrue(trainerOptional.isPresent());
    }

    @When("the trainee requests to add training with: trainerUsername {string}, duration {string}, date {string}, trainingName {string}")
    public void theTraineeRequestsToAddTrainingWithTrainerUsernameDurationDateTrainingName(String trainerUsername, String duration, String date, String trainingName) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("trainerUsername", trainerUsername);
        requestBody.put("duration", duration);
        requestBody.put("date", date);
        requestBody.put("trainingName", trainingName);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(TRAINEE_TRAINING_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(requestBody.toString()))
                .andReturn();
    }

    @And("the training for {string} should be added")
    public void theTrainingForShouldBeAdded(String traineeUsername) {
        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUser_Username(traineeUsername);

        assertTrue(traineeOptional.isPresent());
        traineeOptional.ifPresent(trainee -> {
            List<Training> trainingList = trainee.getTrainingList();

            assertEquals(2, trainingList.size());
        });
    }

    @Then("the response status for training add should be {int}")
    public void theResponseStatusForTrainingAddShouldBe(int status) {
        assertEquals(status, mvcResult.getResponse().getStatus());
    }

    @Given("Given the trainer {string} is available in the system")
    public void givenTheTrainerIsAvailableInTheSystem(String trainerUsername) {
        Optional<Trainer> trainerOptional = trainerRepository.getTrainerByUser_Username(trainerUsername);

        assertTrue(trainerOptional.isPresent());
    }
}
