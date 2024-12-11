Feature: Training Event Summary

  Scenario: Retrieve a summary of all trainers
    Given the training event service is running
    When I request the summary of trainers
    Then I should receive a list of trainer summaries
    And the response status should be 200
    And the training duration records should be structured properly