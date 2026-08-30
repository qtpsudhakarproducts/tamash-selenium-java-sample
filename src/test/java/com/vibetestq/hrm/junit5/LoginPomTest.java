package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.pages.pom.DashboardPage;
import com.vibetestq.hrm.pages.pom.LoginPage;
import io.github.qtpsudhakarproducts.tamash.junit.UseTamashSelenium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 + Page Object Model + the {@code @UseTamashSelenium} extension.
 *
 * <p>The extension launches a self-healing {@link WebDriver} and injects it as the test-method
 * parameter — a fresh one per method. Nothing else to configure: every {@code findElement} through
 * that driver (directly or via a Page Object) is healing-aware.
 */
@UseTamashSelenium
@DisplayName("Login - JUnit 5, Page Object Model")
class LoginPomTest {

  @Test
  @DisplayName("valid credentials land on the dashboard")
  void validLogin(WebDriver driver) {
    DashboardPage dashboard = new LoginPage(driver).open().loginAsAdmin();

    assertTrue(dashboard.isLoaded(), "dashboard grid should be visible after login");
    assertEquals("Dashboard", dashboard.headerTitle());
  }

  @Test
  @DisplayName("invalid credentials show an error and stay on the login page")
  void invalidLogin(WebDriver driver) {
    LoginPage login = new LoginPage(driver).open()
        .loginExpectingError("no-such-user", "wrong-password");

    assertEquals("Invalid credentials", login.errorMessage());
  }

  @Test
  @DisplayName("submitting an empty form shows required-field errors")
  void emptyLogin(WebDriver driver) {
    LoginPage login = new LoginPage(driver).open().submitEmpty();

    assertTrue(login.hasRequiredFieldErrors());
  }

  @Test
  @DisplayName("logout returns to the login screen")
  void logout(WebDriver driver) {
    DashboardPage dashboard = new LoginPage(driver).open().loginAsAdmin();
    dashboard.logout();

    assertTrue(driver.getCurrentUrl().contains("/auth/login"));
  }
}
