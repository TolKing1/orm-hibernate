package org.tolking.integration.controller.steps.TrainingType;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.tolking.entity.TrainingType;
import org.tolking.repository.TrainingTypeRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class GetTrainingTypeListSteps {
    private final WebApplicationContext webApplicationContext;
    private final TrainingTypeRepository trainingTypeRepository;

    private MockMvc mockMvc;
    private MvcResult mvcResult;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Given("the training types are available in the system")
    public void theTrainingTypesAreAvailableInTheSystem() {
        List<TrainingType> trainingTypes = trainingTypeRepository.getAllBy();
        assertNotNull(trainingTypes);
        assertNotEquals(0, trainingTypes.size());
    }

    @When("the user requests the list of training types")
    public void theUserRequestsTheListOfTrainingTypes() throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/training-type"))
                .andReturn();
    }

    @And("the response status for training type should be {int}")
    public void theResponseStatusForTrainingTypeShouldBe(int status) {
        assertEquals(status, mvcResult.getResponse().getStatus());
    }

    @Then("the response should contain a list of training types with id and name")
    public void theResponseShouldContainAListOfTrainingTypesWithIdAndName() throws JSONException, UnsupportedEncodingException {
        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertNotNull(jsonArray);
        assertDoesNotThrow(() -> jsonArray.getJSONObject(0).get("id"));
        assertDoesNotThrow(() -> jsonArray.getJSONObject(0).get("name"));
        assertNotEquals(0, jsonArray.length());
    }
}
