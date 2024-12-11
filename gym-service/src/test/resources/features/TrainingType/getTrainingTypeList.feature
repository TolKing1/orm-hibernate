Feature: Training Type Retrieval

  Scenario: Retrieve all training types
    Given the training types are available in the system
    When the user requests the list of training types
    Then the response should contain a list of training types with id and name
    And the response status for training type should be 200