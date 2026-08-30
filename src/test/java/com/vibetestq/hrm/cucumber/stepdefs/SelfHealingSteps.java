package com.vibetestq.hrm.cucumber.stepdefs;

import com.vibetestq.hrm.pages.broken.BrokenAddEmployeePage;
import com.vibetestq.hrm.pages.factory.DashboardPageF;
import com.vibetestq.hrm.pages.factory.LoginPageF;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.qtpsudhakarproducts.tamash.healer.Healer;
import io.github.qtpsudhakarproducts.tamash.healer.SelfHealingReport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Self-healing demo steps. {@link BrokenAddEmployeePage}'s locators are wrong on purpose; the
 * scenario passes only because tamash-selenium heals them at runtime (heals are also attached to
 * the Cucumber scenario by the tamash glue).
 */
public class SelfHealingSteps {

  private BrokenAddEmployeePage brokenPage;

  @Given("I am on the Add Employee form as an administrator")
  public void onAddEmployeeForm() {
    new LoginPageF(World.driver()).open().loginAsAdmin();
    new DashboardPageF(World.driver()).goToAddEmployee();
    brokenPage = new BrokenAddEmployeePage(World.driver());
  }

  @When("I fill the form using stale locators for {string} {string}")
  public void fillWithStaleLocators(String first, String last) {
    brokenPage.enterName(first, last).save();
  }

  @Then("the employee is still saved via self-healing")
  public void savedViaHealing() {
    assertTrue(brokenPage.isSaved());
    List<SelfHealingReport> healed = Healer.getHealingReports().stream()
        .filter(SelfHealingReport::isHealed)
        .toList();
    assertFalse(healed.isEmpty(), "at least one locator should have healed this run");
  }
}
