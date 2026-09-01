package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.pages.pom.LoginPage;
import com.vibetestq.hrm.pages.pom.pim.EmployeeListPage;
import com.vibetestq.qtpsudhakar.tamash.junit.UseTamashSelenium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** JUnit 5 + Page Object Model: navigate to PIM &rarr; Employee List and read the results grid. */
@UseTamashSelenium
@DisplayName("Employee List - JUnit 5, Page Object Model")
class EmployeeListTest {

  @Test
  void employeeListShowsRecords(WebDriver driver) {
    EmployeeListPage list = new LoginPage(driver).open()
        .loginAsAdmin()
        .goToEmployeeList();

    assertTrue(list.hasResults(), "the employee list should show at least one record");
  }
}
