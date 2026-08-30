package com.vibetestq.hrm.pages.pom;

import com.vibetestq.hrm.config.AppConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * OrangeHRM login page — Page Object Model style.
 *
 * <p>Each locator is bound to a descriptive {@code By} field. tamash-selenium decodes that field
 * name into the element description it shows in heal logs and hands to the healer
 * ({@code usernameInput} → "Username (input)"), so a well-named field is the whole "annotation"
 * you need — there is no {@code .describe()} call.
 */
public class LoginPage extends BasePage {

  private final By usernameInput = By.name("username");
  private final By passwordInput = By.name("password");
  private final By loginButton   = By.cssSelector("button[type='submit']");
  private final By errorAlert    = By.cssSelector(".oxd-alert-content-text");
  private final By requiredFieldErrors = By.cssSelector(".oxd-input-field-error-message");

  public LoginPage(WebDriver driver) {
    super(driver);
  }

  public LoginPage open() {
    driver.get(AppConfig.loginUrl());
    visible(usernameInput);
    return this;
  }

  public DashboardPage loginAs(String username, String password) {
    type(usernameInput, username);
    type(passwordInput, password);
    click(loginButton);
    return new DashboardPage(driver).waitUntilLoaded();
  }

  /** Log in with the configured admin credentials. */
  public DashboardPage loginAsAdmin() {
    return loginAs(AppConfig.USERNAME, AppConfig.PASSWORD);
  }

  /** Submit bad credentials and stay on the login page. */
  public LoginPage loginExpectingError(String username, String password) {
    type(usernameInput, username);
    type(passwordInput, password);
    click(loginButton);
    return this;
  }

  public LoginPage submitEmpty() {
    click(loginButton);
    return this;
  }

  public String errorMessage() {
    return visible(errorAlert).getText().trim();
  }

  public boolean hasRequiredFieldErrors() {
    return !driver.findElements(requiredFieldErrors).isEmpty();
  }
}
