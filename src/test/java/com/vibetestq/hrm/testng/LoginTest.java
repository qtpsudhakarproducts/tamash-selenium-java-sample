package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.pages.pom.DashboardPage;
import com.vibetestq.hrm.pages.pom.LoginPage;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/** TestNG + Page Object Model login coverage. */
public class LoginTest extends BaseTest {

  @Test
  public void validLoginLandsOnDashboard() {
    DashboardPage dashboard = loginAsAdmin();
    assertTrue(dashboard.isLoaded());
    assertEquals(dashboard.headerTitle(), "Dashboard");
  }

  @Test
  public void invalidLoginShowsError() {
    LoginPage login = new LoginPage(driver).open()
        .loginExpectingError("nobody", "nothing");
    assertEquals(login.errorMessage(), "Invalid credentials");
  }

  @Test
  public void logoutReturnsToLogin() {
    loginAsAdmin().logout();
    assertTrue(driver.getCurrentUrl().contains("/auth/login"));
  }
}
