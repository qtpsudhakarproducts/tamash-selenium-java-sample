package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.config.AppConfig;
import com.vibetestq.hrm.keyword.WebUtil;
import io.github.qtpsudhakarproducts.tamash.Tamash;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Keyword-driven style: tests call a {@link WebUtil} action layer, not the driver directly.
 *
 * <p>Healing follows through the helper — tamash-selenium walks the call stack back to the frame
 * that passed the locator. Where the locator has no meaningful name at the call site,
 * {@link Tamash#hint(String)} — or {@code WebUtil}'s name-taking overloads — give the healer a real
 * description instead of a guess.
 */
public class KeywordDrivenTest extends BaseTest {

  @Test
  public void addEmployeeThroughAKeywordLayer() {
    WebUtil web = new WebUtil(driver);

    driver.get(AppConfig.loginUrl());
    web.type(By.name("username"), AppConfig.USERNAME, "Username field");
    web.type(By.name("password"), AppConfig.PASSWORD, "Password field");
    web.click(By.cssSelector("button[type='submit']"), "Login button");

    web.click(By.xpath("//ul[contains(@class,'oxd-main-menu')]//span[normalize-space()='PIM']"), "PIM menu");
    web.click(By.xpath("//a[normalize-space()='Add Employee']"), "Add Employee tab");

    // A locator with no descriptive name at the call site — hint it explicitly.
    By firstName = By.name("firstName");
    try (var scope = Tamash.hint("First name field")) {
      web.type(firstName, "Keyword");
    }
    web.type(By.name("lastName"), "Driven", "Last name field");
    web.click(By.cssSelector("form button[type='submit']"), "Save button");

    // web.text(...) waits for visibility — the form navigates to Personal Details on save.
    assertEquals(web.text(By.xpath("//h6[normalize-space()='Personal Details']")), "Personal Details");
  }
}
