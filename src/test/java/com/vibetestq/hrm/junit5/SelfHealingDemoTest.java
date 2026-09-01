package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.pages.broken.BrokenAddEmployeePage;
import com.vibetestq.hrm.pages.pom.LoginPage;
import com.vibetestq.qtpsudhakar.tamash.healer.Healer;
import com.vibetestq.qtpsudhakar.tamash.healer.SelfHealingReport;
import com.vibetestq.qtpsudhakar.tamash.junit.UseTamashSelenium;
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
 *
 * <p>The assertions are on the <em>healing</em> — each broken locator recovered to the right
 * durable selector and resolved to the real input. Whether OrangeHRM then persists the record is
 * a property of that (free-tier, sometimes slow) demo app, not of tamash-selenium, so it is only
 * logged.
 */
@UseTamashSelenium
@DisplayName("Self-healing demo - JUnit 5")
class SelfHealingDemoTest {

  @Test
  void brokenLocatorsAreHealedAtRuntime(WebDriver driver) {
    new LoginPage(driver).open().loginAsAdmin().goToAddEmployee();

    BrokenAddEmployeePage page = new BrokenAddEmployeePage(driver);
    page.enterName("Healed", "Employee");

    // The broken locators resolved to the actual inputs — the typed values landed there.
    assertEquals("Healed", page.realFieldValue("firstName"));
    assertEquals("Employee", page.realFieldValue("lastName"));

    page.save();

    List<SelfHealingReport> healed = Healer.getHealingReports().stream()
        .filter(SelfHealingReport::isHealed)
        .toList();

    System.out.println("[demo] heals this run:");
    healed.forEach(h -> System.out.println(
        "  " + h.getDescription() + "  ->  " + h.getSuggestedSelector() + "   [" + h.getProvider() + "]"));

    assertHealed(healed, "First Name (textbox)", "By.name(\"firstName\")");
    assertHealed(healed, "Last Name (textbox)", "By.name(\"lastName\")");
    assertTrue(healed.stream().anyMatch(h -> "Save (button)".equals(h.getDescription())),
        "the broken Save button locator should have healed");

    if (!page.isSaved()) {
      System.out.println("[demo] note: the OrangeHRM save round-trip did not confirm within 30s "
          + "(the free-tier demo app was slow) — the healing above still succeeded");
    }
  }

  private static void assertHealed(List<SelfHealingReport> healed, String description, String expectedSelector) {
    assertTrue(
        healed.stream().anyMatch(h -> description.equals(h.getDescription())
            && expectedSelector.equals(h.getSuggestedSelector())),
        () -> "expected a heal of \"" + description + "\" to " + expectedSelector + ", got: "
            + healed.stream().map(h -> h.getDescription() + "->" + h.getSuggestedSelector()).toList());
  }
}
