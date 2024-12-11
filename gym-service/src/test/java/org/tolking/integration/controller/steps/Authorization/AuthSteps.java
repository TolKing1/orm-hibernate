package org.tolking.integration.controller.steps.Authorization;

import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.tolking.entity.User;
import org.tolking.integration.config.TestContext;
import org.tolking.repository.UserRepository;
import org.tolking.service.JWTService;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthSteps {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final TestContext testContext;

    @Given("the trainer is authenticated with username {string}")
    public void theTrainerIsAuthenticated(String username) {
        setToken(username);
    }

    @Given("the trainee is authenticated with username {string}")
    public void theTraineeIsAuthenticatedWithUsername(String username) {
        setToken(username);
    }

    private void setToken(String username) {
        User user = userRepository.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        String token = jwtService.generateToken(user);
        testContext.setJwtToken(token);
    }


    @Given("the trainer has been registered with username {string}")
    public void theTrainerHasBeenRegisteredWithUsername(String username) {
        Optional<User> user = userRepository.findByUsernameAndIsActiveTrue(username);

        Assertions.assertFalse(user.isEmpty());
    }

    @Given("the trainee has been registered with username {string}")
    public void theTraineeHasBeenRegisteredWithUsername(String username) {
        Optional<User> user = userRepository.findByUsernameAndIsActiveTrue(username);

        Assertions.assertFalse(user.isEmpty());
    }

    @Given("the user has been registered with username {string}")
    public void theUserHasBeenRegisteredWithUsername(String username) {
        Optional<User> user = userRepository.findByUsernameAndIsActiveTrue(username);

        Assertions.assertFalse(user.isEmpty());
    }


}