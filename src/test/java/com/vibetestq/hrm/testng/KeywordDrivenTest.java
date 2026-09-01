package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.config.AppConfig;
import com.vibetestq.hrm.keyword.WebUtil;
import com.vibetestq.qtpsudhakar.tamash.Tamash;
import com.vibetestq.qtpsudhakar.tamash.healer.Healer;
import com.vibetestq.qtpsudhakar.tamash.healer.SelfHealingReport;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Keyword-driven style: tests call a {@link WebUtil} action layer, not the driver directly.
 *
 * <p>Healing follows through the helper — tamash-selenium walks the call stack back to the frame
 * that passed the locator and decodes the argument name. Where the locator is built from data and
 * has no meaningful name at the call site, {@link Tamash#hint(String)} gives the healer a real
 * description. Here the "first name" locator is deliberately broken and passed with only a hint —
 * it still heals.
 */
public class KeywordDrivenTest extends BaseTest {

  @Test
  public void healsThroughAKeywordLayerViaHint() {
    WebUtil web = new WebUtil(driver);

    driver.get(AppConfig.loginUrl());
    web.type(By.name("username"), AppConfig.USERNAME, "Username field");
    web.type(By.name("password"), AppConfig.PASSWORD, "Password field");
    web.click(By.cssSelector("button[type='submit']"), "Login button");

    web.click(By.xpath("//ul[contains(@class,'oxd-main-menu')]//span[normalize-space()='PIM']"), "PIM menu");
    web.click(By.xpath("//a[normalize-space()='Add Employee']"), "Add Employee tab");
    web.waitVisible(By.name("firstName"));

    // Broken locator, built as data, passed into the util with only a hint for a name.
    // The hint is what the healer matches against the page — keep it to what a user would read
    // ("First Name"), not an internal label.
    By brokenFirstName = By.cssSelector("input[name='first_name']");   // real: name='firstName'
    try (var scope = Tamash.hint("First Name (textbox)")) {
      web.type(brokenFirstName, "Keyword");
    }
    web.type(By.name("lastName"), "Driven", "Last name field");

    // the hint drove the heal, and it resolved to the real input
    assertEquals(web.value(By.name("firstName")), "Keyword");

    List<SelfHealingReport> healed = Healer.getHealingReports().stream()
        .filter(SelfHealingReport::isHealed)
        .toList();
    assertTrue(
        healed.stream().anyMatch(h -> "First Name (textbox)".equals(h.getDescription())),
        "expected a heal described by the hint, got: "
            + healed.stream().map(SelfHealingReport::getDescription).toList());
  }
}
