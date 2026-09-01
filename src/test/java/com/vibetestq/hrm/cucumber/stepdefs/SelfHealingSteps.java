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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Self-healing demo steps. {@link BrokenAddEmployeePage}'s locators are wrong on purpose; they
 * recover at runtime and heals are attached to the Cucumber scenario by the tamash glue. Assertions
 * are on the healing — the OrangeHRM save round-trip is only logged (flaky free-tier app).
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
    brokenPage.enterName(first, last);
    assertEquals(first, brokenPage.realFieldValue("firstName"));
    assertEquals(last, brokenPage.realFieldValue("lastName"));
    brokenPage.save();
  }

  @Then("every stale locator was healed at runtime")
  public void everyLocatorHealed() {
    List<SelfHealingReport> healed = Healer.getHealingReports().stream()
        .filter(SelfHealingReport::isHealed)
        .toList();
    assertTrue(healed.stream().anyMatch(h -> "First Name (textbox)".equals(h.getDescription())));
    assertTrue(healed.stream().anyMatch(h -> "Last Name (textbox)".equals(h.getDescription())));
    assertTrue(healed.stream().anyMatch(h -> "Save (button)".equals(h.getDescription())));

    if (!brokenPage.isSaved()) {
      System.out.println("[demo] note: OrangeHRM save round-trip did not confirm within 30s "
          + "(free-tier demo app was slow) — the healing above still succeeded");
    }
  }
}
