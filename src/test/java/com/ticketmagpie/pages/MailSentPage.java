package com.ticketmagpie.pages;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.openqa.selenium.WebDriver;

public class MailSentPage extends Page {

  public MailSentPage(WebDriver driver) {
    super(driver);

    assertThat(driver.getPageSource(), containsString("We have sent you a password recovery email."));
  }

}
