package com.vibetestq.hrm.pages.pom.pim;

import com.vibetestq.hrm.pages.pom.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** PIM &rarr; Add Employee form (Page Object Model style). */
public class AddEmployeePage extends BasePage {

  private final By firstNameInput  = By.name("firstName");
  private final By middleNameInput = By.name("middleName");
  private final By lastNameInput   = By.name("lastName");
  private final By saveButton      = By.cssSelector("form button[type='submit']");
  private final By personalDetailsHeader = By.xpath("//h6[normalize-space()='Personal Details']");
  private final By successToast    = By.cssSelector(".oxd-toast");

  public AddEmployeePage(WebDriver driver) {
    super(driver);
    visible(firstNameInput);
  }

  public AddEmployeePage enterName(String first, String last) {
    type(firstNameInput, first);
    type(lastNameInput, last);
    return this;
  }

  public AddEmployeePage enterName(String first, String middle, String last) {
    type(firstNameInput, first);
    type(middleNameInput, middle);
    type(lastNameInput, last);
    return this;
  }

  public AddEmployeePage save() {
    click(saveButton);
    return this;
  }

  /** True once the save landed — the "Successfully Saved" toast, or the Personal Details tab. */
  public boolean isSaved() {
    return waitForAnyPresent(30, successToast, personalDetailsHeader);
  }

  public String toastMessage() {
    return visible(successToast).getText().trim();
  }
}
