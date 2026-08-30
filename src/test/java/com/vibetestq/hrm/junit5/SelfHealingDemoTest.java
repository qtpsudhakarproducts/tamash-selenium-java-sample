package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.pages.broken.BrokenAddEmployeePage;
import com.vibetestq.hrm.pages.pom.LoginPage;
import io.github.qtpsudhakarproducts.tamash.healer.Healer;
import io.github.qtpsudhakarproducts.tamash.healer.SelfHealingReport;
import io.github.qtpsudhakarproducts.tamash.junit.UseTamashSelenium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The self-healing demo. {@link BrokenAddEmployeePage} uses locators that are wrong on purpose
 * ({@code By.name("first_name")} instead of {@code firstName}, etc.). With healing on (the default)
 * the flow still completes; run it with {@code -DHEALER_ENABLED=false} and it fails at the first
 * field.
 */
@UseTamashSelenium
@DisplayName("Self-healing demo - JUnit 5")
class SelfHealingDemoTest {

  @Test
  void brokenLocatorsAreHealedAtRuntime(WebDriver driver) {
    new LoginPage(driver).open().loginAsAdmin().goToAddEmployee();

    BrokenAddEmployeePage page = new BrokenAddEmployeePage(driver);
    page.enterName("Healed", "Employee").save();

    assertTrue(page.isSaved(), "the employee should be saved despite every locator being wrong");

    List<SelfHealingReport> healed = Healer.getHealingReports().stream()
        .filter(SelfHealingReport::isHealed)
        .toList();
    assertFalse(healed.isEmpty(), "at least one locator should have healed");

    System.out.println("[demo] heals this run:");
    healed.forEach(h -> System.out.println(
        "  " + h.getDescription() + "  ->  " + h.getSuggestedSelector() + "   [" + h.getProvider() + "]"));
  }
}
