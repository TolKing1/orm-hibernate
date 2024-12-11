Feature: User login

  Scenario: Trainer: Login
    Given the trainer has been registered with username "testTrainer"
    When User login with username "testTrainer" and password "Password123"
    Then User should receive a token
    And the response status for login should be 200

  Scenario: Trainee: Login
    Given the trainee has been registered with username "testTrainee"
    When User login with username "testTrainer" and password "Password123"
    Then User should receive a token
    And the response status for login should be 200

  Scenario: User login with invalid credentials
    Given the user has been registered with username "testTrainer"
    When User login with username "testTrainer" and password "Password"
    Then the response status for login should be 401