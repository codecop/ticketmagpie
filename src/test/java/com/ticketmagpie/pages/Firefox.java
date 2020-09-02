package com.ticketmagpie.pages;

import com.ticketmagpie.TestEnvironment;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Firefox extends ExternalResource {

  private static final String GECKO_DRIVER_FILE = "webdriver.gecko.driver";

  private WebDriver driver;

  @Override
  protected void before() {
    boolean isWindows = System.getProperty("os.name").startsWith("Windows");
    System.setProperty(GECKO_DRIVER_FILE, "./src/test/resources/geckodriver" + ((isWindows) ? ".exe" : ""));
    FirefoxOptions options = new FirefoxOptions();
    if (TestEnvironment.inEnvironment()) {
      options.setHeadless(true);
    }
    driver = new FirefoxDriver(options);
  }

  @Override
  protected void after() {
    if (driver != null) {
      driver.quit();
    }
    System.clearProperty(GECKO_DRIVER_FILE);
  }

  public WelcomePage open(String baseUrl) {
    return WelcomePage.open(driver, baseUrl);
  }

}
