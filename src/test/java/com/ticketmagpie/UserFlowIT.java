package com.ticketmagpie;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=11111", "spring.profiles.active=hsqldb" })
public class UserFlowIT {

  @Value("${local.server.port}")
  private int port;

  private static WebDriver driver;

  private String base;

  @BeforeClass
  public static void openBrowser() {
    System.setProperty("webdriver.gecko.driver", "./src/test/resources/geckodriver-0.25.0-win32.exe");
    driver = new FirefoxDriver();
  }

  @AfterClass
  public static void closeBrowser() {
    if (driver != null) {
      driver.quit();
    }
    System.clearProperty("webdriver.gecko.driver");
  }

  @Before
  public void setUp() {
    this.base = "http://127.0.0.1:" + port + '/';
  }

  @Test
  public void shouldRegisterAndLogInAndLogout() {
    driver.get(this.base);

    String welcome = driver.findElement(By.tagName("h2")).getText();
    assertEquals("All the shiny tickets are here!", welcome);

    driver.findElement(By.partialLinkText("Register")).click();
    // should see register page
    assertThat(driver.getPageSource(), containsString("Please enter the user name and password you would like"));

    driver.findElement(By.xpath("//input[@name='username']")).sendKeys("junit");
    driver.findElement(By.xpath("//input[@name='password']")).sendKeys("password");
    driver.findElement(By.xpath("//input[@type='submit']")).click();

    // should see login page
    assertThat(driver.getPageSource(), containsString("Please enter your user name and password"));
    assertThat(driver.getPageSource(), not(containsString("junit")));

    driver.findElement(By.xpath("//input[@name='username']")).sendKeys("junit");
    driver.findElement(By.xpath("//input[@name='password']")).sendKeys("password");
    driver.findElement(By.xpath("//input[@type='submit']")).click();

    // should be logged in
    assertNotNull(driver.findElement(By.partialLinkText("junit"))); // exists

    welcome = driver.findElement(By.tagName("h2")).getText();
    assertEquals("All the shiny tickets are here!", welcome);

    // my stuff  
    driver.findElement(By.partialLinkText("junit")).click();
    // should see my user logged in
    assertThat(driver.getPageSource(), containsString("Hello <span>" + "junit"));
    
    driver.findElement(By.xpath("//input[@value='Sign Out']")).click();
    
    // should see login page
    assertThat(driver.getPageSource(), containsString("Please enter your user name and password"));
    assertThat(driver.getPageSource(), not(containsString("junit")));
  }
}
