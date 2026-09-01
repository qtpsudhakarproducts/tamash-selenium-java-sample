package com.vibetestq.hrm.junit5;

import com.vibetestq.hrm.pages.factory.LoginPageF;
import com.vibetestq.hrm.pages.factory.pim.AddEmployeePageF;
import com.vibetestq.qtpsudhakar.tamash.junit.UseTamashSelenium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit 5 + PageFactory / {@code @FindBy}: the same Add Employee flow, but the pages use
 * {@code @FindBy} fields wired with {@code TamashPageFactory.initElements}.
 */
@UseTamashSelenium
@DisplayName("Add Employee - JUnit 5, PageFactory / @FindBy")
class AddEmployeeFactoryTest {

  @Test
  void addsAnEmployee(WebDriver driver) {
    AddEmployeePageF addEmployee = new LoginPageF(driver).open()
        .loginAsAdmin()
        .goToAddEmployee();

    addEmployee.enterName("Factory", "Employee").save();

    assertTrue(addEmployee.isSaved());
  }
}
