# tamash-selenium-java-sample

[![CI](https://github.com/qtpsudhakarproducts/tamash-selenium-java-sample/actions/workflows/ci.yml/badge.svg)](https://github.com/qtpsudhakarproducts/tamash-selenium-java-sample/actions/workflows/ci.yml)

Worked examples of [**tamash-selenium**](https://github.com/qtpsudhakarproducts/tamash-selenium)
([Maven Central](https://central.sonatype.com/artifact/com.vibetestq.qtpsudhakar/tamash-selenium)) —
self-healing for Selenium Java — in every common test style, run against the live OrangeHRM instance
at **https://qtpsudhakar-vibetestq-hrm.up.railway.app/**.

| Style | Where |
|---|---|
| **JUnit 5** (`@UseTamashSelenium` extension) | [`src/test/java/com/vibetestq/hrm/junit5/`](src/test/java/com/vibetestq/hrm/junit5) |
| **TestNG** (`TamashSeleniumTestNgTest` base class) | [`src/test/java/com/vibetestq/hrm/testng/`](src/test/java/com/vibetestq/hrm/testng) |
| **Cucumber BDD** (`com.vibetestq.qtpsudhakar.tamash.cucumber` glue) | [`src/test/java/com/vibetestq/hrm/cucumber/`](src/test/java/com/vibetestq/hrm/cucumber) + [`src/test/resources/features/`](src/test/resources/features) |
| **Page Object Model** (plain `By` fields) | [`pages/pom/`](src/test/java/com/vibetestq/hrm/pages/pom) |
| **PageFactory / `@FindBy`** (`TamashPageFactory.initElements`) | [`pages/factory/`](src/test/java/com/vibetestq/hrm/pages/factory) |
| **Keyword-driven** (`WebUtil` + `Tamash.hint`) | [`keyword/`](src/test/java/com/vibetestq/hrm/keyword), [`testng/KeywordDrivenTest`](src/test/java/com/vibetestq/hrm/testng/KeywordDrivenTest.java) |
| **Data-driven** | `junit5/ParameterizedLoginTest`, `testng/DataDrivenLoginTest` |
| **No integration** (`SelfHealingDriver.wrap` only) | [`config/PlainDriverFactory`](src/test/java/com/vibetestq/hrm/config/PlainDriverFactory.java), `junit5/PlainWrapTest` |
| **Self-healing demo** (deliberately broken locators) | [`pages/broken/BrokenAddEmployeePage`](src/test/java/com/vibetestq/hrm/pages/broken/BrokenAddEmployeePage.java), `*/SelfHealingDemoTest`, `features/self_healing.feature` |

## Requirements

- **JDK 21+**
- Maven 3.9+
- Chrome / Firefox / Edge installed (Selenium Manager fetches the driver binary automatically).

`tamash-selenium` resolves from Maven Central — nothing to build or install first.

## Run

```bash
# JUnit 5 + Cucumber (the default Surefire run, JUnit Platform)
mvn test

# TestNG (separate provider — activated by a profile)
mvn test -Ptestng

# one class / one test
mvn test -Dtest=LoginPomTest
mvn test -Ptestng -Dtest=AddEmployeePomTest

# watch it work: run headed
mvn test -Dheadless=false
```

## CI

[`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs on every push/PR and weekly:

- **`tamash` job** — `mvn test` (JUnit 5 + Cucumber) and `mvn test -Ptestng`, headless Chrome,
  rule-based provider, **no secrets**. This is the always-on gate.
- **`ai-providers` job** — runs `SelfHealingDemoTest` against `openai` / `anthropic` / `gemini`,
  but only for a provider whose API key is configured as a repo secret. To enable one:

  ```bash
  gh secret set OPENAI_API_KEY --repo qtpsudhakarproducts/tamash-selenium-java-sample
  # optional: gh variable set OPENAI_MODEL --body gpt-4o-mini
  ```

The suite hits a live app, so a red run can mean OrangeHRM is down or slow rather than a real
regression — `-Dsurefire.rerunFailingTestsCount=2` absorbs the transient failures.

Everything is wired for zero configuration: `HEALER_PROVIDER` defaults to the rule-based **`tamash`**
provider (no API key, no network, no tokens). To use an AI provider instead, copy `.env.example` to
`.env` and set the keys, or pass `-Dtamash.provider=<name>`.

### Trying an AI provider

```bash
# GitHub Copilot subscription — needs the `copilot` CLI installed & signed in.
# (pom.xml already declares the optional com.github:copilot-sdk-java for this.)
mvn test -Dtest=SelfHealingDemoTest -Dtamash.provider=copilot-subscription

# Claude subscription — needs CLAUDE_CODE_OAUTH_TOKEN from `claude setup-token`.
export CLAUDE_CODE_OAUTH_TOKEN=...
mvn test -Dtest=SelfHealingDemoTest -Dtamash.provider=claude-subscription
```

### Last verified green against the live app

| Command | Result |
|---|---|
| `mvn test` (`tamash` provider) | 18 (JUnit 5 12 + Cucumber 6) |
| `mvn test -Ptestng` (`tamash` provider) | 10 |

**`SelfHealingDemoTest` across every provider** — each resolves the demo's three broken locators to
the same durable selectors (`By.name("firstName")`, `By.name("lastName")`, the real Save button):

| `-Dtamash.provider=` | Model | Result | ~tokens / heal |
|---|---|---|---|
| `tamash` | rule-based, no AI | ✓ | 0 |
| `ollama` | gpt-oss:120b | ✓ | ~4.2k |
| `openai` | gpt-4o-mini | ✓ | ~4.0k |
| `anthropic` | claude-haiku-4-5 | ✓ | ~4.5k |
| `gemini` | gemini-flash-lite-latest | ✓ | ~5.5k |
| `claude-subscription` | claude-haiku-4-5 (OAuth, no key) | ✓ | ~4.5k |
| `copilot-subscription` | Copilot CLI default (no key) | ✓ | ~0.45k |

Note: use a **`-flash-lite`** Gemini model. A full `-flash` model (e.g. `gemini-3.6-flash`) thinks
by default and takes 15–35 s for a 5k-token selector lookup — it times out. `-flash-lite` returns
in ~2 s. tamash-selenium sends `reasoning_effort: low` on the Gemini surface either way;
`GEMINI_THINKING=on` restores the model default.

### Single module, on purpose

tamash-selenium recovers a locator's human description by reading the Page Object **source file**
relative to the test's working directory. In a multi-module build the pages would sit outside the
test module's tree and that lookup fails, so pages and tests share one source root here. JUnit 5 +
Cucumber run under the default JUnit Platform provider; TestNG needs its own provider, hence the
`-Ptestng` profile (the two providers can't both run in a single Surefire pass).

## The self-healing demo

[`BrokenAddEmployeePage`](src/test/java/com/vibetestq/hrm/pages/broken/BrokenAddEmployeePage.java)
uses locators that are wrong on purpose:

```java
private final By firstNameTextbox = By.name("first_name");        // real: firstName
private final By lastNameTextbox  = By.cssSelector("input#last-name"); // real: name=lastName
private final By saveButton       = By.xpath("//button[normalize-space()='Save Employee']");
```

Run `mvn test -Dtest=SelfHealingDemoTest` (or `-Ptestng`) and the flow still completes — the console
shows:

```
[self-healer] .../BrokenAddEmployeePage.java:49 — driver.findElement "First Name (textbox)"
    -> HEALED [provider=tamash, suggested="By.name("firstName")"]
[demo] heals this run:
  First Name (textbox)  ->  By.name("firstName")   [tamash]
  Last Name (textbox)   ->  By.name("lastName")    [tamash]
  Save (button)         ->  By.cssSelector("button.oxd-button...")   [tamash]
```

Run it with **`-DHEALER_ENABLED=false`** and it fails at the first field — healing recovers a locator
that moved, it never masks one that is genuinely gone.

The test asserts on the **healing** — each broken locator recovered to the right durable selector
and resolved to the real input (the typed value landed there). Whether OrangeHRM then persists the
record is a property of that free-tier demo app (sometimes slow), so it's only logged.

## Landing the fixes: `apply-heals`

After a run that healed something:

```bash
mvn exec:java -Dexec.args="doctor"                    # pre-flight checks
mvn exec:java -Dexec.args="apply-heals --dry-run"     # preview the By.… / @FindBy rewrites
mvn exec:java -Dexec.args="apply-heals --yes"         # write them + a verify-heals script
sh .tamash-selenium/verify-heals.sh                   # re-run affected tests with healing OFF
```

`.tamash-selenium/heals.jsonl` records every heal (broken selector, structured suggestion, and the
ready-to-read `newLocator` / `newFindBy` forms).

## HTML step report

```bash
mvn test -DTAMASH_REPORT=target/tamash-report.html
```

Per-test timeline, which steps healed (recovered selector, provider, token cost), and the DOM
snapshot on any unrecovered failure.

## A note on console noise during SPA loads

OrangeHRM is a Vue single-page app; navigating between sections takes a second or two. A locator
polled inside a `WebDriverWait` while the next screen is still rendering will occasionally print a
`NOT healed [ai_declined]` line before the element appears and the wait succeeds. tamash-selenium
defers the first few such failures and suppresses the repeats; a line that persists well into a wait
is a real, still-broken locator worth looking at.
