Feature: Update not assigned trainer list

  Scenario:Trainee: Successfully update trainer list for trainee
    Given the trainers are available in the system
    And the trainee is authenticated with username "testTrainee"
    When the user requests trainer list with request of list of trainers
    Then trainer list should be updated for trainee "testTrainee"
    And the response status for trainer list should be 200

  Scenario:Trainee: Update trainer list for trainee with not existing trainer
    Given the trainers are available in the system
    And the trainee is authenticated with username "testTrainee"
    When the user requests trainer list with request of list of trainers with not existing trainer
    And the response status for trainer list should be 404