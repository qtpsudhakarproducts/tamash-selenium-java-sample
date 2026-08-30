package com.vibetestq.hrm.pages.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base for the Page Object Model pages — plain {@code By} fields, resolved through the
 * self-healing driver. OrangeHRM is a Vue single-page app, so every interaction goes through an
 * explicit {@link WebDriverWait}; the implicit wait is pinned to 0 by tamash-selenium.
 */
public abstract class BasePage {

  protected final WebDriver driver;
  protected final WebDriverWait wait;

  protected BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
  }

  protected WebElement visible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected WebElement clickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected void type(By locator, String text) {
    WebElement el = visible(locator);
    el.clear();
    el.sendKeys(text);
  }

  protected void click(By locator) {
    clickable(locator).click();
  }

  protected boolean isDisplayed(By locator) {
    try {
      return visible(locator).isDisplayed();
    } catch (RuntimeException e) {
      return false;
    }
  }
}
