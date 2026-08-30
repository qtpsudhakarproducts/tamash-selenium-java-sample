package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.pages.factory.LoginPageF;
import com.vibetestq.hrm.pages.factory.pim.AddEmployeePageF;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** TestNG + PageFactory / {@code @FindBy}: add an employee through the factory-wired pages. */
public class AddEmployeeFactoryTest extends BaseTest {

  @Test
  public void addsAnEmployee() {
    AddEmployeePageF addEmployee = new LoginPageF(driver).open()
        .loginAsAdmin()
        .goToAddEmployee();

    addEmployee.enterName("TestNG", "Factory").save();
    assertTrue(addEmployee.isSaved());
  }
}
