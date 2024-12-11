package org.tolking.integration.controller.steps.Authorization;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.tolking.service.JWTService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
@Transactional
public class LoginSteps {
    private final JWTService jwtService;
    private final MockMvc mockMvc;

    private MvcResult mvcResult;


    @When("User login with username {string} and password {string}")
    public void UserLoginWithUsernameAndPassword(String arg0, String arg1) throws Exception {
        JSONObject request = new JSONObject();
        request.put("username", arg0);
        request.put("password", arg1);

        this.mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(request.toString())
                        .contentType("application/json"))
                .andReturn();
    }

    @Then("User should receive a token")
    public void userShouldReceiveAToken() throws Exception {
        String token = mvcResult.getResponse().getContentAsString();
        assertNotNull(jwtService.extractUserName(token));
    }

    @And("the response status for login should be {int}")
    public void theResponseStatusForLoginShouldBe(int status) {
        assertEquals(status, mvcResult.getResponse().getStatus());
    }

}
