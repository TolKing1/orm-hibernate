package org.tolking.integration.controller.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.tolking.dto.TrainerSummaryDTO;
import org.tolking.enums.ActionType;
import org.tolking.external_dto.TrainingEventDTO;
import org.tolking.service.TrainingEventService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class TrainingEventSummarySteps {

    private final WebApplicationContext webApplicationContext;
    private final TrainingEventService trainingEventService;
    private final ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private MvcResult mvcResult;

    @Given("the training event service is running")
    public void the_training_event_service_is_running() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        TrainingEventDTO trainingEventDTO = new TrainingEventDTO();
        trainingEventDTO.setTrainerUserUsername("test");
        trainingEventDTO.setTrainerUserFirstName("Test");
        trainingEventDTO.setTrainerUserLastName("User");
        trainingEventDTO.setTrainerUserIsActive(true);
        trainingEventDTO.setDate(LocalDate.now());
        trainingEventDTO.setDuration(200);
        trainingEventDTO.setActionType(ActionType.ADD);

        trainingEventService.create(trainingEventDTO);
    }

    @When("I request the summary of trainers")
    public void i_request_the_summary_of_trainers() throws Exception {
        this.mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/event/summary"))
                                .andExpect(status().isOk())
                                .andReturn();
    }

    @Then("I should receive a list of trainer summaries")
    public void i_should_receive_a_list_of_trainer_summaries() throws Exception {
        List<TrainerSummaryDTO> trainerSummaries = parseResponse(mvcResult);
        Assertions.assertNotNull(trainerSummaries, "Trainer summaries list should not be null");
        Assertions.assertFalse(trainerSummaries.isEmpty(), "Trainer summaries list should not be empty");
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer statusCode) throws Exception {
        Assertions.assertEquals(statusCode, mvcResult.getResponse().getStatus(), "Response status should match the expected value");
    }

    @Then("the training duration records should be structured properly")
    public void the_training_duration_records_should_be_structured_properly() throws Exception {
        List<TrainerSummaryDTO> trainerSummaries = parseResponse(mvcResult);

        for (TrainerSummaryDTO trainerSummary : trainerSummaries) {
            Map<Integer, Map<Integer, Integer>> years = trainerSummary.getYears();
            Assertions.assertNotNull(years, "Years map should not be null");

            years.forEach((year, months) -> {
                Assertions.assertTrue(year > 0, "Year must be a positive integer");
                months.forEach((month, duration) -> {
                    Assertions.assertTrue(month >= 1 && month <= 12, "Month must be between 1 and 12");
                    Assertions.assertTrue(duration >= 0, "Duration must be a non-negative number");
                });
            });
        }
    }

    private List<TrainerSummaryDTO> parseResponse(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }
}