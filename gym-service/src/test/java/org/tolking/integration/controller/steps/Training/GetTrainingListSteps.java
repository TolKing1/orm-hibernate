package org.tolking.integration.controller.steps.Training;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.integration.config.TestContext;
import org.tolking.repository.TraineeRepository;
import org.tolking.repository.TrainerRepository;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.tolking.integration.util.GlobalConstants.AUTHORIZATION_HEADER;
import static org.tolking.integration.util.GlobalConstants.CONTENT_TYPE_JSON;

@RequiredArgsConstructor
public class GetTrainingListSteps {
    private final TestContext testContext;
    private final MockMvc mockMvc;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final String TRAINER_TRAINING_LIST_URL = "/trainer/training/criteria";
    private final String TRAINEE_TRAINING_LIST_URL = "/trainee/training/criteria";
    private MvcResult mvcResult;

    @Given("the training list are assigned to trainee {string}")
    public void theTrainingListAreAssignedToTrainee(String username) {
        Trainee trainee = traineeRepository.getTraineeByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        assertNotEquals(0, trainee.getTrainingList().size());
    }

    @When("the trainee requests the list of training list without criteria")
    public void theUserRequestsTheListOfTrainingListWithoutCriteria() throws Exception {
        mvcResult = getMvcResult(TRAINEE_TRAINING_LIST_URL);
    }

    @Then("the response should contain a list of training")
    public void theResponseShouldContainAListOfTrainingTypes() throws UnsupportedEncodingException, JSONException {
        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertEquals(1, jsonArray.length());

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        assertNotNull(jsonObject.getString("trainingName"));
        assertNotNull(jsonObject.getString("date"));
        assertNotNull(jsonObject.getString("duration"));
        assertNotNull(jsonObject.getString("traineeUsername"));
        assertNotNull(jsonObject.getString("trainingType"));

    }

    @And("the response status for training list should be {int}")
    public void theResponseStatusForTrainingListShouldBe(int status) {
        assertEquals(status, mvcResult.getResponse().getStatus());
    }

    @When("the trainee requests the list of training list with criteria: periodFrom {string}, periodTo {string}, trainerUsername {string}")
    public void theUserRequestsTheListOfTrainingListWithCriteriaPeriodFromPeriodToTrainerUsername(String from, String to, String trainerUsername) throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TRAINEE_TRAINING_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON)
                        .param("periodFrom", from)
                        .param("periodTo", to)
                        .param("trainerUsername", trainerUsername))
                .andReturn();
    }

    @Given("the training list are assigned to trainer {string}")
    public void theTrainingListAreAssignedToTrainer(String username) {
        Trainer trainer = trainerRepository.getTrainerByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        assertNotEquals(0, trainer.getTrainingList().size());
    }

    @When("the trainer requests the list of training list with criteria: periodFrom {string}, periodTo {string}, traineeUsername {string}")
    public void theTrainerRequestsTheListOfTrainingListWithCriteriaPeriodFromPeriodToTraineeUsername(String periodFrom, String periodTo, String traineeUsername) throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TRAINER_TRAINING_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON)
                        .param("periodFrom", periodFrom)
                        .param("periodTo", periodTo)
                        .param("traineeName", traineeUsername))
                .andReturn();
    }

    @When("the trainer requests the list of training list without criteria")
    public void theTrainerRequestsTheListOfTrainingListWithoutCriteria() throws Exception {
        mvcResult = getMvcResult(TRAINER_TRAINING_LIST_URL);
    }

    private MvcResult getMvcResult(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON))
                .andReturn();
    }
}
