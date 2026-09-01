package com.vibetestq.hrm.testng.support;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Attaches {@link RetryOnce} to every {@code @Test} in the suite, so a single flaky failure against
 * the live app doesn't fail the build. Registered in {@code src/test/resources/testng.xml}.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RetryListener implements IAnnotationTransformer {

  @Override
  public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
                        Method testMethod) {
    Class<? extends org.testng.IRetryAnalyzer> existing = annotation.getRetryAnalyzerClass();
    if (existing == null
        || existing == org.testng.internal.annotations.DisabledRetryAnalyzer.class) {
      annotation.setRetryAnalyzer(RetryOnce.class);
    }
  }
}
