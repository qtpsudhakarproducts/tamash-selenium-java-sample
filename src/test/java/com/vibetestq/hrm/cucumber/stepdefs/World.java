package com.vibetestq.hrm.cucumber.stepdefs;

import io.github.qtpsudhakarproducts.tamash.cucumber.TamashSeleniumScenario;
import org.openqa.selenium.WebDriver;

/**
 * Tiny shared accessor so step classes don't each repeat the static import. The driver itself is
 * created and torn down per scenario by {@code TamashSeleniumCucumberHooks} (in the glue path).
 */
final class World {
  private World() {}

  static WebDriver driver() {
    return TamashSeleniumScenario.driver();
  }
}
