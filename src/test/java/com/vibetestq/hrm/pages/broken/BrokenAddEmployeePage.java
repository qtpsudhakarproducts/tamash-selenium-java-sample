package com.vibetestq.hrm.pages.broken;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * A copy of the Add Employee page with <b>deliberately broken locators</b>, used by the
 * self-healing demo tests in every framework module.
 *
 * <p>The real OrangeHRM 5.x fields are {@code name="firstName"} / {@code name="lastName"} and a
 * {@code <button type="submit">Save</button>}. The locators below are wrong on purpose. Callers
 * open the form first (which waits on the real fields), then this page does a direct
 * {@code driver.findElement(brokenLocator)} — the clean self-healing entry point. tamash-selenium:
 * <ol>
 *   <li>catches the {@code NoSuchElementException},</li>
 *   <li>snapshots the page's accessibility tree,</li>
 *   <li>matches the decoded field name ("First Name (textbox)") to the real element,</li>
 *   <li>derives a durable locator ({@code By.name("firstName")}), retries, and continues,</li>
 *   <li>records it in {@code .tamash-selenium/heals.jsonl} for {@code apply-heals} to land.</li>
 * </ol>
 * Run the same test with {@code -DHEALER_ENABLED=false} and it fails at the first field — that is
 * the point: healing never masks a locator that is genuinely gone, it recovers one that moved.
 */
public class BrokenAddEmployeePage {

  private final WebDriver driver;
  private final WebDriverWait wait;

  // real: By.name("firstName")
  private final By firstNameTextbox = By.name("first_name");
  // real: By.name("lastName")
  private final By lastNameTextbox  = By.cssSelector("input#last-name");
  // real: form button[type='submit']
  private final By saveButton       = By.xpath("//button[normalize-space()='Save Employee']");
  private final By personalDetailsHeader = By.xpath("//h6[normalize-space()='Personal Details']");

  public BrokenAddEmployeePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    // The form is already open (the caller navigated here); make sure it has rendered.
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName")));
  }

  public BrokenAddEmployeePage enterName(String first, String last) {
    driver.findElement(firstNameTextbox).sendKeys(first);   // heals to the real first-name field
    driver.findElement(lastNameTextbox).sendKeys(last);     // heals to the real last-name field
    return this;
  }

  public BrokenAddEmployeePage save() {
    driver.findElement(saveButton).click();                 // heals to the real Save button
    return this;
  }

  public boolean isSaved() {
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
    } catch (RuntimeException e) {
      return false;
    }
  }
}
