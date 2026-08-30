package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.pages.broken.BrokenAddEmployeePage;
import io.github.qtpsudhakarproducts.tamash.healer.Healer;
import io.github.qtpsudhakarproducts.tamash.healer.SelfHealingReport;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Self-healing demo (TestNG). {@link BrokenAddEmployeePage}'s locators are wrong on purpose; with
 * healing on the flow still completes. Run with {@code -DHEALER_ENABLED=false} to see it fail.
 */
public class SelfHealingDemoTest extends BaseTest {

  @Test
  public void brokenLocatorsAreHealedAtRuntime() {
    loginAsAdmin().goToAddEmployee();

    BrokenAddEmployeePage page = new BrokenAddEmployeePage(driver);
    page.enterName("Healed", "OnTestNg").save();
    assertTrue(page.isSaved(), "employee saved despite broken locators");

    List<SelfHealingReport> healed = Healer.getHealingReports().stream()
        .filter(SelfHealingReport::isHealed)
        .toList();
    assertFalse(healed.isEmpty(), "at least one locator should have healed");

    System.out.println("[demo] heals this run:");
    healed.forEach(h -> System.out.println(
        "  " + h.getDescription() + "  ->  " + h.getSuggestedSelector() + "   [" + h.getProvider() + "]"));
  }
}
