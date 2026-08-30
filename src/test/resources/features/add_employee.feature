Feature: PIM - Add Employee

  Scenario: Add a new employee
    Given I am signed in as an administrator
    When I add an employee named "Cucumber" "Bdd"
    Then the employee is saved
