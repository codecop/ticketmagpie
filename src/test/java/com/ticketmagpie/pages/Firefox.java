package com.ticketmagpie.pages;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Firefox extends ExternalResource {

  private static final String GECKO_DRIVER_FILE = "webdriver.gecko.driver";

  private WebDriver driver;

  @Override
  protected void before() {
    System.setProperty(GECKO_DRIVER_FILE, "./src/test/resources/geckodriver-0.25.0-win32.exe");
    driver = new FirefoxDriver();
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
