Feature: Not assigned trainer list

  Scenario:Trainee: Retrieve all not assigned trainers
    Given the trainers are available in the system
    And the trainee is authenticated with username "testTrainee"
    When the user requests the list of not assigned trainers
    Then the response should contain a list of not assigned trainers
    And the response status for trainer list should be 200