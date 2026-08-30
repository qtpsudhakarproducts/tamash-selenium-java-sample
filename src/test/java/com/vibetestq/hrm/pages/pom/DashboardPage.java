package com.vibetestq.hrm.pages.pom;

import com.vibetestq.hrm.pages.pom.pim.AddEmployeePage;
import com.vibetestq.hrm.pages.pom.pim.EmployeeListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** OrangeHRM dashboard + the left navigation menu shared by every authenticated page. */
public class DashboardPage extends BasePage {

  private final By dashboardGrid   = By.cssSelector(".oxd-grid-3");
  private final By breadcrumbTitle = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
  private final By userDropdown    = By.cssSelector(".oxd-userdropdown-tab");
  private final By logoutLink      = By.cssSelector("a[href='/web/index.php/auth/logout']");
  private final By menuSearchInput = By.cssSelector(".oxd-main-menu-search input");

  public DashboardPage(WebDriver driver) {
    super(driver);
  }

  DashboardPage waitUntilLoaded() {
    visible(dashboardGrid);
    return this;
  }

  public boolean isLoaded() {
    return isDisplayed(dashboardGrid);
  }

  public String headerTitle() {
    return visible(breadcrumbTitle).getText().trim();
  }

  /** Click a top-level item in the left menu (e.g. "PIM", "Admin", "Leave"). */
  public DashboardPage openMenu(String name) {
    type(menuSearchInput, name);
    click(By.xpath("//ul[contains(@class,'oxd-main-menu')]//span[normalize-space()='" + name + "']"));
    return this;
  }

  public AddEmployeePage goToAddEmployee() {
    openMenu("PIM");
    click(By.xpath("//a[normalize-space()='Add Employee']"));
    return new AddEmployeePage(driver);
  }

  public EmployeeListPage goToEmployeeList() {
    openMenu("PIM");
    click(By.xpath("//a[normalize-space()='Employee List']"));
    return new EmployeeListPage(driver);
  }

  public void logout() {
    click(userDropdown);
    click(logoutLink);
  }
}
