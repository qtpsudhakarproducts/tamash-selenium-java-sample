package com.vibetestq.hrm.pages.factory;

import com.vibetestq.hrm.pages.factory.pim.AddEmployeePageF;
import com.vibetestq.qtpsudhakar.tamash.pagefactory.TamashPageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** OrangeHRM dashboard + left menu — PageFactory / {@code @FindBy} style. */
public class DashboardPageF {

  private final WebDriver driver;
  private final WebDriverWait wait;

  @FindBy(css = ".oxd-grid-3")
  private WebElement dashboardGrid;

  @FindBy(css = ".oxd-topbar-header-breadcrumb h6")
  private WebElement breadcrumbTitle;

  @FindBy(css = ".oxd-userdropdown-tab")
  private WebElement userMenu;

  @FindBy(css = "a[href='/web/index.php/auth/logout']")
  private WebElement logoutLink;

  @FindBy(css = ".oxd-main-menu-search input")
  private WebElement menuSearchBox;

  public DashboardPageF(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    TamashPageFactory.initElements(driver, this);
  }

  public boolean isLoaded() {
    try {
      return wait.until(ExpectedConditions.visibilityOf(dashboardGrid)).isDisplayed();
    } catch (RuntimeException e) {
      return false;
    }
  }

  public String headerTitle() {
    return wait.until(ExpectedConditions.visibilityOf(breadcrumbTitle)).getText().trim();
  }

  public AddEmployeePageF goToAddEmployee() {
    wait.until(ExpectedConditions.visibilityOf(menuSearchBox));
    menuSearchBox.clear();
    menuSearchBox.sendKeys("PIM");
    wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//ul[contains(@class,'oxd-main-menu')]//span[normalize-space()='PIM']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//a[normalize-space()='Add Employee']"))).click();
    return new AddEmployeePageF(driver);
  }

  public void logout() {
    wait.until(ExpectedConditions.elementToBeClickable(userMenu)).click();
    wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
  }
}
