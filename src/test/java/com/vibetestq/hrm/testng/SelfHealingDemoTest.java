package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.pages.broken.BrokenAddEmployeePage;
import com.vibetestq.qtpsudhakar.tamash.healer.Healer;
import com.vibetestq.qtpsudhakar.tamash.healer.SelfHealingReport;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Self-healing demo (TestNG). {@link BrokenAddEmployeePage}'s locators are wrong on purpose; with
 * healing on they recover at runtime. Run with {@code -DHEALER_ENABLED=false} to see it fail.
 * Assertions are on the healing; the OrangeHRM save round-trip is only logged (flaky free-tier app).
 */
public class SelfHealingDemoTest extends BaseTest {

  @Test
  public void brokenLocatorsAreHealedAtRuntime() {
    loginAsAdmin().goToAddEmployee();

    BrokenAddEmployeePage page = new BrokenAddEmployeePage(driver);
    page.enterName("Healed", "OnTestNg");

    assertEquals(page.realFieldValue("firstName"), "Healed");
    assertEquals(page.realFieldValue("lastName"), "OnTestNg");

    page.save();

    List<SelfHealingReport> healed = Healer.getHealingReports().stream()
        .filter(SelfHealingReport::isHealed)
        .toList();

    System.out.println("[demo] heals this run:");
    healed.forEach(h -> System.out.println(
        "  " + h.getDescription() + "  ->  " + h.getSuggestedSelector() + "   [" + h.getProvider() + "]"));

    assertTrue(healedTo(healed, "First Name (textbox)", "By.name(\"firstName\")"), "First Name should heal");
    assertTrue(healedTo(healed, "Last Name (textbox)", "By.name(\"lastName\")"), "Last Name should heal");
    assertTrue(healed.stream().anyMatch(h -> "Save (button)".equals(h.getDescription())),
        "the broken Save button locator should have healed");

    if (!page.isSaved()) {
      System.out.println("[demo] note: OrangeHRM save round-trip did not confirm within 30s "
          + "(free-tier demo app was slow) — the healing above still succeeded");
    }
  }

  private static boolean healedTo(List<SelfHealingReport> healed, String description, String selector) {
    return healed.stream().anyMatch(h -> description.equals(h.getDescription())
        && selector.equals(h.getSuggestedSelector()));
  }
}
