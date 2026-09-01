package com.vibetestq.hrm.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Cucumber runner (JUnit Platform).
 *
 * <p>The glue path includes {@code com.vibetestq.qtpsudhakar.tamash.cucumber} — that package's
 * hooks launch a self-healing {@link org.openqa.selenium.WebDriver} per scenario (read it with
 * {@code TamashSeleniumScenario.driver()}), attach heals to the scenario, and drive
 * {@code apply-heals} tracking + the HTML step report.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME,
    value = "com.vibetestq.hrm.cucumber.stepdefs,com.vibetestq.qtpsudhakar.tamash.cucumber")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, summary")
public class RunCucumberTest {
}
