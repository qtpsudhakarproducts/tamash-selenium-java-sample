package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.config.PlainDriverFactory;
import com.vibetestq.hrm.pages.pom.DashboardPage;
import com.vibetestq.hrm.pages.pom.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * tamash-selenium with <b>no framework integration</b> — you own the driver lifecycle.
 *
 * <p>This is the JUnit 4 / custom-base-class / "we already have our own setup" pattern. The whole
 * integration is {@link PlainDriverFactory#create()}, which calls
 * {@code SelfHealingDriver.wrap(new ChromeDriver(...))}. No extension, no annotation.
 */
@DisplayName("Plain SelfHealingDriver.wrap - JUnit 5, no extension")
class PlainWrapTest {

  private WebDriver driver;

  @BeforeEach
  void setUp() {
    driver = PlainDriverFactory.create();
  }

  @AfterEach
  void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void logsInWithASelfHealingDriver() {
    DashboardPage dashboard = new LoginPage(driver).open().loginAsAdmin();
    assertTrue(dashboard.isLoaded());
  }
}
