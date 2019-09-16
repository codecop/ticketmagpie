package com.ticketmagpie;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ticketmagpie.pages.LoginPage;
import com.ticketmagpie.pages.RegisterPage;
import com.ticketmagpie.pages.UserPage;
import com.ticketmagpie.pages.WelcomePage;

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
    WelcomePage welcome = WelcomePage.open(driver, base);
    RegisterPage register = welcome.clickRegister();
    LoginPage login = register.register("junit", "password");
    welcome = login.login("junit", "password");
    UserPage user = welcome.clickUser("junit");
    user.clickLogout();
  }
}
