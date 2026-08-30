package com.vibetestq.hrm.pages.pom.pim;

import com.vibetestq.hrm.pages.pom.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** PIM &rarr; Employee List. Loads with every employee shown; this page just reads the results grid. */
public class EmployeeListPage extends BasePage {

  private final By resultRows = By.cssSelector(".oxd-table-body .oxd-table-card");

  public EmployeeListPage(WebDriver driver) {
    super(driver);
    // The table shell renders first, then an async call fills it — wait for at least one row.
    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(resultRows, 0));
  }

  public int rowCount() {
    return driver.findElements(resultRows).size();
  }

  public boolean hasResults() {
    return rowCount() > 0;
  }
}
