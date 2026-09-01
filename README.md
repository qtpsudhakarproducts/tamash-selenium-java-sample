# tamash-selenium-java-sample

[![CI](https://github.com/qtpsudhakarproducts/tamash-selenium-java-sample/actions/workflows/ci.yml/badge.svg)](https://github.com/qtpsudhakarproducts/tamash-selenium-java-sample/actions/workflows/ci.yml)
[![Docs](https://img.shields.io/badge/docs-site-1a73e8.svg)](https://qtpsudhakarproducts.github.io/tamash-selenium/)
[![Maven Central](https://img.shields.io/maven-central/v/com.vibetestq.qtpsudhakar/tamash-selenium.svg)](https://central.sonatype.com/artifact/com.vibetestq.qtpsudhakar/tamash-selenium)

Worked examples of [**tamash-selenium**](https://github.com/qtpsudhakarproducts/tamash-selenium) —
self-healing for Selenium Java — in every common test style, run against a live OrangeHRM instance
at **https://qtpsudhakar-vibetestq-hrm.up.railway.app/**.

## 📖 Documentation

The **library documentation lives on its own site** — install, all providers with a full `.env`
template, the healing model, `doctor` / `apply-heals` / `init-skill`, and the complete env-var and
CLI reference:

| | |
|---|---|
| **Full guide** | <https://qtpsudhakarproducts.github.io/tamash-selenium/guide.html> |
| Overview & support | <https://qtpsudhakarproducts.github.io/tamash-selenium/> |
| Library source & issues | <https://github.com/qtpsudhakarproducts/tamash-selenium> |
| Maven Central | <https://central.sonatype.com/artifact/com.vibetestq.qtpsudhakar/tamash-selenium> |

**This repo is the "see it wired into a real framework" companion to that guide** — clone it, run
it, copy the pattern that matches your suite.

## What's in here

Every file is a real, passing test against the live app. Pick the row that matches how your own
suite is built.

### Test styles

| Style | Start here |
|---|---|
| **JUnit 5** — `@UseTamashSelenium`, `WebDriver` injected as a parameter | [`junit5/LoginPomTest`](src/test/java/com/vibetestq/hrm/junit5/LoginPomTest.java), [`junit5/AddEmployeePomTest`](src/test/java/com/vibetestq/hrm/junit5/AddEmployeePomTest.java) |
| **TestNG** — `extends TamashSeleniumTestNgTest`, `protected WebDriver driver` | [`testng/LoginTest`](src/test/java/com/vibetestq/hrm/testng/LoginTest.java), [`testng/BaseTest`](src/test/java/com/vibetestq/hrm/testng/BaseTest.java) |
| **Cucumber BDD** — `com.vibetestq.qtpsudhakar.tamash.cucumber` glue, `TamashSeleniumScenario.driver()` | [`cucumber/`](src/test/java/com/vibetestq/hrm/cucumber) + [`features/`](src/test/resources/features) |
| **Page Object Model** — plain `By` fields, `PageFactory` not involved | [`pages/pom/`](src/test/java/com/vibetestq/hrm/pages/pom) |
| **PageFactory / `@FindBy`** — `TamashPageFactory.initElements(driver, this)` | [`pages/factory/`](src/test/java/com/vibetestq/hrm/pages/factory), [`junit5/AddEmployeeFactoryTest`](src/test/java/com/vibetestq/hrm/junit5/AddEmployeeFactoryTest.java) |
| **Keyword-driven** — a `WebUtil` layer + `Tamash.hint(...)` where names don't reach the call site | [`keyword/WebUtil`](src/test/java/com/vibetestq/hrm/keyword/WebUtil.java), [`testng/KeywordDrivenTest`](src/test/java/com/vibetestq/hrm/testng/KeywordDrivenTest.java) |
| **Data-driven** | [`junit5/ParameterizedLoginTest`](src/test/java/com/vibetestq/hrm/junit5/ParameterizedLoginTest.java), [`testng/DataDrivenLoginTest`](src/test/java/com/vibetestq/hrm/testng/DataDrivenLoginTest.java) |
| **No framework integration** — just `SelfHealingDriver.wrap(driver)` | [`config/PlainDriverFactory`](src/test/java/com/vibetestq/hrm/config/PlainDriverFactory.java), [`junit5/PlainWrapTest`](src/test/java/com/vibetestq/hrm/junit5/PlainWrapTest.java) |

### Self-healing demo (deliberately broken locators)

| | |
|---|---|
| The broken page | [`pages/broken/BrokenAddEmployeePage`](src/test/java/com/vibetestq/hrm/pages/broken/BrokenAddEmployeePage.java) |
| The tests | [`junit5/SelfHealingDemoTest`](src/test/java/com/vibetestq/hrm/junit5/SelfHealingDemoTest.java), [`testng/SelfHealingDemoTest`](src/test/java/com/vibetestq/hrm/testng/SelfHealingDemoTest.java), [`features/self_healing.feature`](src/test/resources/features/self_healing.feature) |

### Support scaffolding (not tamash-specific — just how this sample is built)

`config/AppConfig` (URLs/creds from `.env` / system properties), `pages/pom/BasePage`
(a `waitForAnyPresent` helper), `testng/testng.xml`, `junit-platform.properties`.

## Quick start

```bash
git clone https://github.com/qtpsudhakarproducts/tamash-selenium-java-sample
cd tamash-selenium-java-sample
mvn test                       # JUnit 5 + Cucumber, rule-based provider — no keys needed
```

**Requirements:** JDK 21+, Maven 3.9+, and Google Chrome installed (Selenium Manager fetches the
matching driver). `tamash-selenium` resolves from Maven Central — nothing to build first.

### Running

```bash
mvn test                                # JUnit 5 + Cucumber (default Surefire run, JUnit Platform)
mvn test -Ptestng                       # TestNG (separate provider — activated by a profile)

mvn test -Dtest=LoginPomTest            # one class
mvn test -Ptestng -Dtest=AddEmployeePomTest

mvn test -Dheadless=false               # watch it in a real browser window
mvn test -DTAMASH_REPORT=target/tamash-report.html   # + the HTML step report
```

### Using an AI provider instead of the rule-based default

With no configuration, healing uses the rule-based `tamash` provider (no key, no network, no
tokens). To use an AI provider, copy `.env.example` to `.env` and fill it in, or pass
`-Dtamash.provider=<name>`:

```bash
mvn test -Dtest=SelfHealingDemoTest -Dtamash.provider=openai   # + OPENAI_API_KEY, OPENAI_MODEL in .env
mvn test -Dtest=SelfHealingDemoTest -Dtamash.provider=anthropic
mvn test -Dtest=SelfHealingDemoTest -Dtamash.provider=gemini   # use a -flash-lite model

# subscription providers — no API key
mvn test -Dtest=SelfHealingDemoTest -Dtamash.provider=claude-subscription   # needs `claude setup-token` / CLAUDE_CODE_OAUTH_TOKEN
mvn test -Dtest=SelfHealingDemoTest -Dtamash.provider=copilot-subscription  # needs the `copilot` CLI signed in
```

See the [provider section of the guide](https://qtpsudhakarproducts.github.io/tamash-selenium/guide.html#step-2-connect-an-ai-provider)
for the full list and exact variable names.

## The self-healing demo, explained

[`BrokenAddEmployeePage`](src/test/java/com/vibetestq/hrm/pages/broken/BrokenAddEmployeePage.java)
uses locators that are wrong on purpose:

```java
private final By firstNameTextbox = By.name("first_name");             // real: name=firstName
private final By lastNameTextbox  = By.cssSelector("input#last-name");  // real: name=lastName / #ln
private final By saveButton       = By.xpath("//button[normalize-space()='Save Employee']");
```

Run `mvn test -Dtest=SelfHealingDemoTest` (or `-Ptestng`) and the flow still completes — the console
shows each locator being recovered:

```
[self-healer] .../BrokenAddEmployeePage.java — driver.findElement "First Name (textbox)"
    -> HEALED [provider=tamash, suggested="By.name("firstName")"]
[demo] heals this run:
  First Name (textbox)  ->  By.name("firstName")   [tamash]
  Last Name (textbox)   ->  By.name("lastName")    [tamash]
  Save (button)         ->  By.cssSelector("button.oxd-button...")   [tamash]
```

The test asserts on the **healing** — each broken locator recovered to the right durable selector
and resolved to the real input (the typed value landed there). It also runs cleanly with
`-DHEALER_ENABLED=false` **once the locators have been rewritten** by `apply-heals`; run it with
healing off *before* that and it fails at the first field — healing recovers a locator that moved,
it never masks one that is genuinely gone.

## Landing the fixes: `apply-heals`

After a run that healed something:

```bash
mvn exec:java -Dexec.args="doctor"                    # pre-flight checks
mvn exec:java -Dexec.args="apply-heals --dry-run"     # preview the By.… / @FindBy rewrites
mvn exec:java -Dexec.args="apply-heals --yes"         # write them + a verify-heals script
sh .tamash-selenium/verify-heals.sh                   # re-run the affected tests with healing OFF
```

`.tamash-selenium/heals.jsonl` records every heal (broken selector, structured suggestion, and the
ready-to-read `newLocator` / `newFindBy` forms). The CI **`apply-heals` job** below runs this exact
loop on every push.

## CI

[`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs on every push/PR and weekly. Every job
is green on the current build (see [Actions](https://github.com/qtpsudhakarproducts/tamash-selenium-java-sample/actions)):

| Job | What it runs | Secrets |
|---|---|---|
| **`tamash`** (the gate) | `mvn test` (JUnit 5 + Cucumber) and `mvn test -Ptestng`, headless Chrome, rule-based provider | none |
| **`ai-providers`** | `SelfHealingDemoTest` through `openai` / `anthropic` / `gemini`, per provider whose key is a repo/org secret | `*_API_KEY` |
| **`ollama`** | same demo through `ollama` | `OLLAMA_API_KEY` |
| **`apply-heals`** | heal the demo via `anthropic` → `apply-heals --yes` → re-verify with `HEALER_ENABLED=false` | `ANTHROPIC_API_KEY` |

To enable a provider: `gh secret set OPENAI_API_KEY --repo qtpsudhakarproducts/tamash-selenium-java-sample`
(optionally `gh variable set OPENAI_MODEL --body gpt-4o-mini`). The `ai-providers` / `ollama` /
`apply-heals` jobs are `continue-on-error` — a red square there usually means the provider
rate-limited or the live app was slow, not a regression. The `tamash` job is the real gate.

### Provider results (verified against the live app)

`SelfHealingDemoTest` resolves the demo's three broken locators to the same durable selectors
(`By.name("firstName")`, `By.name("lastName")`, the real Save button) on every provider:

| `-Dtamash.provider=` | Model | ~tokens / heal |
|---|---|---|
| `tamash` | rule-based, no AI | 0 |
| `ollama` | gpt-oss:120b | ~4.2k |
| `openai` | gpt-4o-mini | ~4.0k |
| `anthropic` | claude-haiku-4-5 | ~4.5k |
| `gemini` | gemini-flash-lite-latest | ~5.5k |
| `claude-subscription` | claude-haiku-4-5 (OAuth, no key) | ~4.5k |
| `copilot-subscription` | Copilot CLI default (no key) | ~0.45k |

> Use a **`-flash-lite`** Gemini model. A full `-flash` model thinks by default and takes 15–35 s
> for a 5k-token selector lookup — it times out. `-flash-lite` returns in ~2 s. tamash-selenium
> sends `reasoning_effort: low` on the Gemini surface either way; `GEMINI_THINKING=on` restores the
> model default.

## Two things about this sample specifically

**Single module, on purpose.** tamash-selenium recovers a locator's human description by reading
the Page Object **source file** relative to the test's working directory. In a multi-module build
the pages would sit outside the test module's tree and that lookup fails (there's
`TAMASH_SOURCE_ROOTS` for that case), so pages and tests share one source root here. JUnit 5 +
Cucumber run under the default JUnit Platform provider; TestNG needs its own provider, hence the
`-Ptestng` profile.

**Console noise during SPA loads.** OrangeHRM is a Vue single-page app; navigating between sections
takes a second or two. A locator polled inside a `WebDriverWait` while the next screen is still
rendering will occasionally print a `NOT healed [ai_declined]` line before the element appears and
the wait succeeds. tamash-selenium defers the first few such failures and suppresses the repeats; a
line that persists well into a wait is a real, still-broken locator worth looking at.

## License

[Apache License 2.0](LICENSE), same as the library. Copy any of these examples into your own suite
freely.
