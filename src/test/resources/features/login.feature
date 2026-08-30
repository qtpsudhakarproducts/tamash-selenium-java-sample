Feature: OrangeHRM login

  Background:
    Given I am on the OrangeHRM login page

  Scenario: Successful login
    When I sign in with the admin account
    Then I land on the dashboard

  Scenario Outline: Rejected credentials
    When I sign in with username "<username>" and password "<password>"
    Then I see the error "Invalid credentials"

    Examples:
      | username     | password       |
      | Admin        | wrongpass      |
      | unknown-user | Vibetestq@123# |
      | testadmin    | almost-right   |
