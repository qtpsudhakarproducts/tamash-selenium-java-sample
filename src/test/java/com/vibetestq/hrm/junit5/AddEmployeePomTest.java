package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.pages.pom.LoginPage;
import com.vibetestq.hrm.pages.pom.pim.AddEmployeePage;
import com.vibetestq.qtpsudhakar.tamash.junit.UseTamashSelenium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** JUnit 5 + Page Object Model: add an employee through the PIM module. */
@UseTamashSelenium
@DisplayName("Add Employee - JUnit 5, Page Object Model")
class AddEmployeePomTest {

  @Test
  void addsAnEmployee(WebDriver driver) {
    AddEmployeePage addEmployee = new LoginPage(driver).open()
        .loginAsAdmin()
        .goToAddEmployee();

    addEmployee.enterName("Tamash", "Selenium").save();

    assertTrue(addEmployee.isSaved(), "should navigate to the saved employee's Personal Details");
  }
}
