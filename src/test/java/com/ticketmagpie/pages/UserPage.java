package com.ticketmagpie.pages;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.WebDriver;

public class UserPage extends Page {

  public UserPage(WebDriver driver, String username) {
    super(driver);

    assertThat(driver.getPageSource(), containsString("Hello <span>" + username));
  }

}
