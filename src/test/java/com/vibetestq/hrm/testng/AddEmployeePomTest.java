package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.pages.pom.pim.AddEmployeePage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** TestNG + Page Object Model: add an employee. */
public class AddEmployeePomTest extends BaseTest {

  @Test
  public void addsAnEmployee() {
    AddEmployeePage addEmployee = loginAsAdmin().goToAddEmployee();
    addEmployee.enterName("TestNG", "Pom").save();
    assertTrue(addEmployee.isSaved());
  }
}
