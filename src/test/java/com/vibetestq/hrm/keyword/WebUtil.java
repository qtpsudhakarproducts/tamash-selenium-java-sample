package com.vibetestq.hrm.keyword;

import io.github.qtpsudhakarproducts.tamash.Tamash;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * A keyword-driven action layer — the "framework wrapper" many Selenium suites put between the
 * tests and the raw driver.
 *
 * <p>Healing still works through this indirection: tamash-selenium walks up the call stack to the
 * frame that passed the locator in and decodes the argument name. But when the caller has nothing
 * meaningful to decode — a locator built from a string key, an enum, a data table — wrap the action
 * in {@link Tamash#hint(String)} so the healer gets a real description instead of a guess:
 *
 * <pre>{@code
 * try (var s = Tamash.hint("First name field")) {
 *   webUtil.type(locatorFromDataTable, "Tamash");
 * }
 * }</pre>
 */
public class WebUtil {

  private final WebDriver driver;
  private final WebDriverWait wait;

  public WebUtil(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
  }

  public void type(By locator, String text) {
    WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    el.clear();
    el.sendKeys(text);
  }

  /** Same as {@link #type(By, String)} but names the element for the healer explicitly. */
  public void type(By locator, String text, String elementName) {
    try (var scope = Tamash.hint(elementName)) {
      type(locator, text);
    }
  }

  public void click(By locator) {
    wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
  }

  public void click(By locator, String elementName) {
    try (var scope = Tamash.hint(elementName)) {
      click(locator);
    }
  }

  public String text(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
  }

  public boolean isPresent(By locator) {
    // findElements never heals — the right call for a genuine "is it absent?" check.
    return !driver.findElements(locator).isEmpty();
  }
}
