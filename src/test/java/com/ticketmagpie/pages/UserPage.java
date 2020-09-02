package com.ticketmagpie.pages;

import static org.hamcrest.Matchers.containsString;

import org.openqa.selenium.WebDriver;

public class UserPage extends Page {

  public UserPage(WebDriver driver, String username) {
    super(driver);

    assertWaitingThat(WebDriver::getPageSource, containsString("Hello <span>" + username));
  }

}
