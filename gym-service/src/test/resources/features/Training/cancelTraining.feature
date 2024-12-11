Feature: Trainee: Cancel Training

  Scenario: Successfully cancel training
    Given the training list are assigned to trainee "testTrainee" with trainingId
    And the trainee is authenticated with username "testTrainee"
    When the trainee requests to cancel training with trainingId
    Then the response status for training should be 200
    And the training for "testTrainee" should be cancelled

  Scenario: Cancel training with invalid trainingId
    Given the training list are assigned to trainee "testTrainee" with trainingId
    And the trainee is authenticated with username "testTrainee"
    When the trainee requests to cancel training with invalid trainingId
    Then the response status for training should be 404