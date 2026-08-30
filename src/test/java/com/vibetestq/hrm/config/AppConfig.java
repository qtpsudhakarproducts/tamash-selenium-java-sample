package com.vibetestq.hrm.config;

/**
 * App-under-test configuration. Every value is resolved in this order:
 * system property (e.g. {@code -Dhrm.baseUrl=...}) → environment variable ({@code HRM_BASE_URL}) →
 * the built-in default (the shared demo OrangeHRM instance).
 */
public final class AppConfig {
  private AppConfig() {}

  public static final String BASE_URL  = resolve("hrm.baseUrl",  "HRM_BASE_URL",
      "https://qtpsudhakar-vibetestq-hrm.up.railway.app/");
  public static final String USERNAME  = resolve("hrm.username", "HRM_USERNAME", "testadmin");
  public static final String PASSWORD  = resolve("hrm.password", "HRM_PASSWORD", "Vibetestq@123#");

  public static String loginUrl() {
    return trimSlash(BASE_URL) + "/web/index.php/auth/login";
  }

  public static String dashboardUrl() {
    return trimSlash(BASE_URL) + "/web/index.php/dashboard/index";
  }

  private static String resolve(String sysProp, String envVar, String fallback) {
    String v = System.getProperty(sysProp);
    if (v == null || v.isBlank()) {
      v = System.getenv(envVar);
    }
    return (v == null || v.isBlank()) ? fallback : v.trim();
  }

  private static String trimSlash(String s) {
    return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
  }
}
