package com.ticketmagpie.pages;

import static org.hamcrest.Matchers.equalTo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WelcomePage extends Page {

  public static WelcomePage open(WebDriver driver, String base) {
    driver.get(base);

    return new WelcomePage(driver);
  }

  public WelcomePage(WebDriver driver) {
    super(driver);

    assertWaitingThat(d -> d.findElement(By.tagName("h2")).getText(),
            equalTo("All the shiny tickets are here!"));
  }

}
