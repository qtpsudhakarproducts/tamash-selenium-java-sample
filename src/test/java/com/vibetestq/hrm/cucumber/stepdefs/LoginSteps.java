package com.vibetestq.hrm.cucumber.stepdefs;

import com.vibetestq.hrm.config.AppConfig;
import com.vibetestq.hrm.pages.pom.DashboardPage;
import com.vibetestq.hrm.pages.pom.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Login step definitions (Page Object Model). */
public class LoginSteps {

  private LoginPage loginPage;
  private DashboardPage dashboard;

  @Given("I am on the OrangeHRM login page")
  public void iAmOnTheLoginPage() {
    loginPage = new LoginPage(World.driver()).open();
  }

  @When("I sign in with the admin account")
  public void iSignInAsAdmin() {
    dashboard = loginPage.loginAs(AppConfig.USERNAME, AppConfig.PASSWORD);
  }

  @When("I sign in with username {string} and password {string}")
  public void iSignInWith(String username, String password) {
    loginPage.loginExpectingError(username, password);
  }

  @Then("I land on the dashboard")
  public void iLandOnTheDashboard() {
    assertTrue(dashboard.isLoaded());
    assertEquals("Dashboard", dashboard.headerTitle());
  }

  @Then("I see the error {string}")
  public void iSeeTheError(String message) {
    assertEquals(message, loginPage.errorMessage());
  }
}
