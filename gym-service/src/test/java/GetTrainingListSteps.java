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
import org.tolking.integration.config.TestContext;
import org.tolking.repository.TraineeRepository;
import org.tolking.repository.TrainerRepository;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

    @When("the user requests the list of training list without criteria")
    public void theUserRequestsTheListOfTrainingListWithoutCriteria() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(TRAINEE_TRAINING_LIST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + testContext.getJwtToken())
                        .contentType(CONTENT_TYPE_JSON))
                .andReturn();
    }

    @Then("the response should contain a list of training types")
    public void theResponseShouldContainAListOfTrainingTypes() {
    }

    @And("the response status for training list should be {int}")
    public void theResponseStatusForTrainingListShouldBe(int arg0) {
    }

    @When("the user requests the list of training list with criteria: periodFrom {string}, periodTo {string}, trainerUsername {string}")
    public void theUserRequestsTheListOfTrainingListWithCriteriaPeriodFromPeriodToTrainerUsername(String arg0, String arg1, String arg2) {
    }
}
