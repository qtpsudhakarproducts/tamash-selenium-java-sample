package com.vibetestq.hrm.cucumber.stepdefs;

import com.vibetestq.hrm.pages.factory.DashboardPageF;
import com.vibetestq.hrm.pages.factory.LoginPageF;
import com.vibetestq.hrm.pages.factory.pim.AddEmployeePageF;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Add Employee step definitions (PageFactory / {@code @FindBy} pages). */
public class AddEmployeeSteps {

  private AddEmployeePageF addEmployee;

  @Given("I am signed in as an administrator")
  public void iAmSignedIn() {
    new LoginPageF(World.driver()).open().loginAsAdmin();
  }

  @When("I add an employee named {string} {string}")
  public void iAddAnEmployee(String first, String last) {
    addEmployee = new DashboardPageF(World.driver()).goToAddEmployee();
    addEmployee.enterName(first, last).save();
  }

  @Then("the employee is saved")
  public void theEmployeeIsSaved() {
    assertTrue(addEmployee.isSaved());
  }
}
