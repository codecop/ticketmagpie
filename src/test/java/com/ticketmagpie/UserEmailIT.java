package com.ticketmagpie;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.ticketmagpie.pages.Firefox;
import com.ticketmagpie.pages.ForgetPasswordPage;
import com.ticketmagpie.pages.LoginPage;
import com.ticketmagpie.pages.WelcomePage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0", "spring.profiles.active=hsqldb", "mail.smtp.host=127.0.0.1", "mail.smtp.port=11112" })
public class UserEmailIT {

  @ClassRule
  public static Firefox browser = new Firefox();

  @Value("${local.server.port}")
  private int httpPort;

  private String baseUrl;

  @Before
  public void setBaseUrl() {
    baseUrl = TestEnvironment.baseUrl("127.0.0.1", httpPort);
  }

  @Value("${mail.smtp.port}")
  private int smtpPort;

  private SimpleSmtpServer dumbster;

  @Before
  public void openSmtpServer() throws IOException {
    dumbster = SimpleSmtpServer.start(smtpPort);
  }

  @After
  public void closeSmtpServer() {
    dumbster.close();
  }

  @Test
  public void shouldSendForgotPasswordEmail() {
    WelcomePage welcome = browser.open(baseUrl);
    LoginPage login = welcome.clickLogin();
    ForgetPasswordPage forgotPassword = login.clickForgot();
    forgotPassword.submit("danbilling");

    // see https://github.com/kirviq/dumbster
    List<SmtpMessage> emails = dumbster.getReceivedEmails();
    assertThat(emails, hasSize(1));
    SmtpMessage email = emails.get(0);
    assertThat(email.getHeaderValue("Subject"), equalTo("Your Ticketmagpie password"));
    assertThat(email.getBody(), equalTo("Hello danbilling! Have you lost your password? Here it is: t4rd1s1963."));
    assertThat(email.getHeaderValue("To"), equalTo("ticketmagpie.danbilling@yopmail.com"));
  }

}
