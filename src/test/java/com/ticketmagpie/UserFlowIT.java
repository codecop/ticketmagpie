package com.ticketmagpie;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ticketmagpie.pages.Firefox;
import com.ticketmagpie.pages.LoginPage;
import com.ticketmagpie.pages.RegisterPage;
import com.ticketmagpie.pages.UserPage;
import com.ticketmagpie.pages.WelcomePage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0", "spring.profiles.active=hsqldb" })
public class UserFlowIT {

  @ClassRule
  public static Firefox browser = new Firefox();

  @Value("${local.server.port}")
  private int httpPort;

  private String baseUrl;

  @Before
  public void setBaseUrl() {
    baseUrl = "http://127.0.0.1:" + httpPort + '/';
  }

  @Test
  public void shouldRegisterAndLogInAndLogout() {
    WelcomePage welcome = browser.open(baseUrl);
    RegisterPage register = welcome.clickRegister();
    LoginPage login = register.register("junit", "password");
    welcome = login.login("junit", "password");
    UserPage user = welcome.clickUser("junit");
    user.clickLogout();
  }
}
