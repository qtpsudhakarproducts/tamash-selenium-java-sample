package com.vibetestq.hrm.testng.support;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test once. These examples run against a live, free-tier OrangeHRM instance that
 * is occasionally slow — a first-attempt failure is far more often the app hiccupping than a real
 * regression. (Surefire's own {@code rerunFailingTestsCount} is ignored when a {@code testng.xml}
 * suite file drives execution, so the retry is wired here instead — see {@link RetryListener}.)
 */
public class RetryOnce implements IRetryAnalyzer {

  private static final int MAX_RETRIES = 1;
  private int attempts = 0;

  @Override
  public boolean retry(ITestResult result) {
    if (attempts < MAX_RETRIES) {
      attempts++;
      System.out.println("[retry] " + result.getName() + " failed — retrying (attempt "
          + (attempts + 1) + " of " + (MAX_RETRIES + 1) + ")");
      return true;
    }
    return false;
  }
}
