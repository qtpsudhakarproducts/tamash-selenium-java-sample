package com.vibetestq.hrm.testng;

import com.vibetestq.hrm.pages.pom.DashboardPage;
import com.vibetestq.hrm.pages.pom.LoginPage;
import io.github.qtpsudhakarproducts.tamash.testng.TamashSeleniumTestNgTest;

/**
 * TestNG base class. {@link TamashSeleniumTestNgTest} runs the whole driver lifecycle — a fresh
 * self-healing {@code driver} field per {@code @Test} method — and its listener (auto-registered
 * via {@code ServiceLoader}) handles {@code apply-heals} tracking and the HTML step report. No
 * {@code @Listeners}, no entry in {@code testng.xml}.
 */
public abstract class BaseTest extends TamashSeleniumTestNgTest {

  protected DashboardPage loginAsAdmin() {
    return new LoginPage(driver).open().loginAsAdmin();
  }
}
