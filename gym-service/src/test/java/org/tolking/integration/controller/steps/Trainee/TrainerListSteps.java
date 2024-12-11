package org.tolking.integration.controller.steps.Trainee;

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
import org.tolking.integration.config.TestContext;
import org.tolking.repository.TraineeRepository;
import org.tolking.repository.TrainerRepository;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.tolking.integration.util.GlobalConstants.AUTHORIZATION_HEADER;
import static org.tolking.integration.util.GlobalConstants.CONTENT_TYPE_JSON;
import static org.tolking.integration.util.GlobalConstants.DEFAULT_TRAINER_USERNAME;

@RequiredArgsConstructor
public class TrainerListSteps {
    private final TestContext testContext;
    private final MockMvc mockMvc;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final String NOT_ASSIGNED_TRAINER_LIST_URL = "/trainee/trainer/not-assigned";
    private final String TRAINER_LIST_URL = "/trainee/trainer";

    private MvcResult mvcResult;


    @Given("the trainers are available in the system")
    public void theTrainersAreAvailableInTheSystem() {
        long count = trainerRepository.count();
        assertNotEquals(0, count);
    }

    @When("the user requests the list of not assigned trainers")
    public void theUserRequestsTheListOfNotAssignedTrainers() throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(NOT_ASSIGNED_TRAINER_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON))
                .andReturn();
    }

    @When("the user requests trainer list with request of list of trainers")
    public void theUserRequestsTrainerListWithRequestOfListOfTrainers() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", DEFAULT_TRAINER_USERNAME);
        jsonArray.put(jsonObject);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(TRAINER_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(jsonArray.toString()))
                .andReturn();
    }

    @When("the user requests trainer list with request of list of trainers with not existing trainer")
    public void theUserRequestsTrainerListWithRequestOfListOfTrainersWithNotExistingTrainer() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "notExistingTrainer");
        jsonArray.put(jsonObject);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(TRAINER_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(jsonArray.toString()))
                .andReturn();
    }

    @Then("the response should contain a list of not assigned trainers")
    public void theResponseShouldContainAListOfNotAssignedTrainers() throws UnsupportedEncodingException, JSONException {
        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());

        assertNotEquals(0, jsonArray.length());

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        assertNotNull(jsonObject.get("username"));
        assertNotNull(jsonObject.get("firstName"));
        assertNotNull(jsonObject.get("lastName"));
        assertNotNull(jsonObject.get("trainingTypeName"));
    }

    @And("the response status for trainer list should be {int}")
    public void theResponseStatusForTrainerListShouldBe(int status) {
        assertEquals(status, mvcResult.getResponse().getStatus());
    }

    @Then("trainer list should be updated for trainee {string}")
    public void trainerListShouldBeUpdated(String username) {
        Trainee trainee = traineeRepository.getTraineeByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        assertEquals(1, trainee.getTrainerList().size());
    }


}
