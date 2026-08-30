package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.pages.pom.LoginPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/** Data-driven with TestNG's {@code @DataProvider}. Fresh self-healing driver per row. */
public class DataDrivenLoginTest extends BaseTest {

  @DataProvider(name = "badCredentials")
  public Object[][] badCredentials() {
    return new Object[][]{
        {"Admin", "wrongpass"},
        {"unknown-user", "Vibetestq@123#"},
        {"testadmin", "almost-right"},
    };
  }

  @Test(dataProvider = "badCredentials")
  public void rejectsBadCredentials(String username, String password) {
    LoginPage login = new LoginPage(driver).open()
        .loginExpectingError(username, password);
    assertEquals(login.errorMessage(), "Invalid credentials");
  }
}
