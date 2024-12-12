Feature: Trainee: SignUp

  Scenario: SignUp with correct data
    Given the user service is running
    When Trainee signUp with firstName "John", lastname "Doe", dateBirth "2003-12-11" and address "1234"
    Then the response status should be 201
    And the response should contain new username and password

  Scenario: SignUp with incorrect name
    Given the user service is running
    When Trainee signUp with firstName "J", lastname "Doe", dateBirth "2003-12-11" and address "1234"
    Then the response status should be 400

  Scenario: SignUp with incorrect dateBirth
    Given the user service is running
    When Trainee signUp with firstName "John", lastname "Doe", dateBirth "2003-1211" and address "1234"
    Then the response status should be 400