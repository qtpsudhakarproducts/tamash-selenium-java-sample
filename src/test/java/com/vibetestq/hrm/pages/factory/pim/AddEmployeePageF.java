package com.vibetestq.hrm.pages.factory.pim;

import com.vibetestq.qtpsudhakar.tamash.pagefactory.TamashPageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** PIM &rarr; Add Employee form — PageFactory / {@code @FindBy} style. */
public class AddEmployeePageF {

  private final WebDriver driver;
  private final WebDriverWait wait;

  @FindBy(name = "firstName")
  private WebElement firstNameField;

  @FindBy(name = "lastName")
  private WebElement lastNameField;

  @FindBy(css = "form button[type='submit']")
  private WebElement saveButton;

  public AddEmployeePageF(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    TamashPageFactory.initElements(driver, this);
    wait.until(ExpectedConditions.visibilityOf(firstNameField));
  }

  public AddEmployeePageF enterName(String first, String last) {
    firstNameField.clear();
    firstNameField.sendKeys(first);
    lastNameField.clear();
    lastNameField.sendKeys(last);
    return this;
  }

  public AddEmployeePageF save() {
    saveButton.click();
    return this;
  }

  /** True once the save landed. Checked with findElements (never healed) so a slow SPA
   *  navigation doesn't trigger heal attempts on a correct-but-not-yet-present locator. */
  public boolean isSaved() {
    By toast = By.cssSelector(".oxd-toast");
    By header = By.xpath("//h6[normalize-space()='Personal Details']");
    long deadline = System.currentTimeMillis() + 30_000;
    while (System.currentTimeMillis() < deadline) {
      if (!driver.findElements(toast).isEmpty() || !driver.findElements(header).isEmpty()) {
        return true;
      }
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    return false;
  }
}
