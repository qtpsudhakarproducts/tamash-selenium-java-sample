package com.vibetestq.hrm.pages.factory.pim;

import io.github.qtpsudhakarproducts.tamash.pagefactory.TamashPageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** PIM &rarr; Add Employee form — PageFactory / {@code @FindBy} style. */
public class AddEmployeePageF {

  private final WebDriverWait wait;

  @FindBy(name = "firstName")
  private WebElement firstNameField;

  @FindBy(name = "lastName")
  private WebElement lastNameField;

  @FindBy(css = "form button[type='submit']")
  private WebElement saveButton;

  @FindBy(xpath = "//h6[normalize-space()='Personal Details']")
  private WebElement personalDetailsHeader;

  public AddEmployeePageF(WebDriver driver) {
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

  public boolean isSaved() {
    try {
      return wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader)).isDisplayed();
    } catch (RuntimeException e) {
      return false;
    }
  }
}
