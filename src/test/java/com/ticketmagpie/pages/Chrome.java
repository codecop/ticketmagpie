package com.ticketmagpie.pages;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Chrome extends ExternalResource {

  private static final String CHROME_DRIVER_FILE = "webdriver.chrome.driver";

  private WebDriver driver;

  @Override
  protected void before() {
    boolean isWindows = System.getProperty("os.name").startsWith("Windows");
    System.setProperty(CHROME_DRIVER_FILE, "./src/test/resources/chromedriver" + ((isWindows) ? ".exe" : ""));
    ChromeOptions options = new ChromeOptions();
    if (isWindows) {
      options.setBinary("C:\\Program Files\\Ungoogled-Chromium_80\\chrome.exe");
    }
    driver = new ChromeDriver(options);
  }

  @Override
  protected void after() {
    if (driver != null) {
      driver.quit();
    }
    System.clearProperty(CHROME_DRIVER_FILE);
  }

  public WelcomePage open(String baseUrl) {
    return WelcomePage.open(driver, baseUrl);
  }

}
