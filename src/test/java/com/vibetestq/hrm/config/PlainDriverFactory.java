package com.vibetestq.hrm.config;

import com.vibetestq.qtpsudhakar.tamash.SelfHealingDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Builds a self-healing {@link WebDriver} <em>without</em> any framework integration — the pattern
 * you use with plain JUnit 4, a hand-rolled base class, or anywhere you own the lifecycle yourself.
 *
 * <p>The entire integration is the single {@link SelfHealingDriver#wrap(WebDriver)} call: hand the
 * wrapped driver to your tests and Page Objects and every {@code findElement} through it is
 * healing-aware. Browser + headless are read the same env/property names the tamash-selenium
 * lifecycle uses ({@code TAMASH_BROWSER}, {@code HEADLESS}) so all modules behave identically.
 *
 * <p>The JUnit 5 (`@UseTamashSelenium`), TestNG (`TamashSeleniumTestNgTest`) and Cucumber
 * (`TamashSeleniumCucumberHooks`) example modules do <b>not</b> use this class — their integration
 * creates and wraps the driver for them. It exists to show the no-integration path.
 */
public final class PlainDriverFactory {
  private PlainDriverFactory() {}

  public static WebDriver create() {
    String browser = prop("TAMASH_BROWSER", "chrome").toLowerCase();
    boolean headless = !"false".equalsIgnoreCase(prop("HEADLESS", "true"));

    WebDriver raw = switch (browser) {
      case "firefox" -> {
        FirefoxOptions o = new FirefoxOptions();
        if (headless) o.addArguments("-headless");
        yield new org.openqa.selenium.firefox.FirefoxDriver(o);
      }
      case "edge" -> {
        EdgeOptions o = new EdgeOptions();
        if (headless) o.addArguments("--headless=new", "--disable-gpu");
        yield new org.openqa.selenium.edge.EdgeDriver(o);
      }
      default -> {
        ChromeOptions o = new ChromeOptions();
        if (headless) o.addArguments("--headless=new", "--disable-gpu");
        o.addArguments("--window-size=1366,900", "--no-sandbox", "--disable-dev-shm-usage");
        yield new org.openqa.selenium.chrome.ChromeDriver(o);
      }
    };

    // The one line that turns an ordinary driver into a self-healing one.
    return SelfHealingDriver.wrap(raw);
  }

  private static String prop(String name, String fallback) {
    String v = System.getProperty(name);
    if (v == null || v.isBlank()) v = System.getenv(name);
    return (v == null || v.isBlank()) ? fallback : v.trim();
  }
}
