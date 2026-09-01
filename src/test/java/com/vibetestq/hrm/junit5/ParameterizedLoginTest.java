package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.pages.pom.LoginPage;
import com.vibetestq.qtpsudhakar.tamash.junit.UseTamashSelenium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Data-driven with JUnit 5's {@code @ParameterizedTest}. A fresh self-healing driver is injected
 * for every row.
 */
@UseTamashSelenium
@DisplayName("Login validation - JUnit 5, data-driven")
class ParameterizedLoginTest {

  @ParameterizedTest(name = "[{index}] {0}/{1}")
  @CsvSource({
      "Admin,         wrongpass",
      "unknown-user,  Vibetestq@123#",
      "testadmin,     almost-right"
  })
  void rejectsBadCredentials(String username, String password, WebDriver driver) {
    LoginPage login = new LoginPage(driver).open()
        .loginExpectingError(username, password);

    assertEquals("Invalid credentials", login.errorMessage());
  }
}
