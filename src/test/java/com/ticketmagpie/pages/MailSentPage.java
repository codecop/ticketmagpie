package com.ticketmagpie.pages;

import static org.hamcrest.Matchers.containsString;

import org.openqa.selenium.WebDriver;

public class MailSentPage extends Page {

  public MailSentPage(WebDriver driver) {
    super(driver);

    assertWaitingThat(WebDriver::getPageSource, containsString("We have sent you a password recovery email."));
  }

}
