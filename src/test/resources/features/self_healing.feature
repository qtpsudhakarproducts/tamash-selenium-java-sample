Feature: Self-healing locators
  The Add Employee page object used here has locators that are wrong on purpose.
  With HEALER_ENABLED=true (the default) tamash-selenium recovers them at runtime;
  with HEALER_ENABLED=false this scenario fails at the first field.

  Scenario: Broken locators are healed during the run
    Given I am on the Add Employee form as an administrator
    When I fill the form using stale locators for "Self" "Healing"
    Then the employee is still saved via self-healing
