package com.vibetestq.hrm.pages.factory;

import com.vibetestq.hrm.config.AppConfig;
import io.github.qtpsudhakarproducts.tamash.pagefactory.TamashPageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * OrangeHRM login page — PageFactory / {@code @FindBy} style.
 *
 * <p>The only difference from a normal PageFactory page is the constructor: call
 * {@link TamashPageFactory#initElements(WebDriver, Object)} instead of
 * {@code PageFactory.initElements(...)}. That makes each {@code @FindBy} field heal <em>and</em>
 * makes the healer's element description the field name ({@code usernameField} → "Username (field)").
 * {@code apply-heals} rewrites the {@code @FindBy(...)} annotation in place when you land the fix.
 */
public class LoginPageF {

  private final WebDriver driver;
  private final WebDriverWait wait;

  @FindBy(name = "username")
  private WebElement usernameField;

  @FindBy(name = "password")
  private WebElement passwordField;

  @FindBy(css = "button[type='submit']")
  private WebElement loginButton;

  @FindBy(css = ".oxd-alert-content-text")
  private WebElement errorAlert;

  public LoginPageF(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    TamashPageFactory.initElements(driver, this);
  }

  public LoginPageF open() {
    driver.get(AppConfig.loginUrl());
    wait.until(ExpectedConditions.visibilityOf(usernameField));
    return this;
  }

  public DashboardPageF loginAsAdmin() {
    return loginAs(AppConfig.USERNAME, AppConfig.PASSWORD);
  }

  public DashboardPageF loginAs(String username, String password) {
    usernameField.clear();
    usernameField.sendKeys(username);
    passwordField.clear();
    passwordField.sendKeys(password);
    loginButton.click();
    return new DashboardPageF(driver);
  }

  public LoginPageF loginExpectingError(String username, String password) {
    usernameField.sendKeys(username);
    passwordField.sendKeys(password);
    loginButton.click();
    return this;
  }

  public String errorMessage() {
    return wait.until(ExpectedConditions.visibilityOf(errorAlert)).getText().trim();
  }
}
