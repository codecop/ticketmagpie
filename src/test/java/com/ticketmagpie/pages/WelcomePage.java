package com.ticketmagpie.pages;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WelcomePage extends Page {

  public static WelcomePage open(WebDriver driver, String base) {
    driver.get(base);

    return new WelcomePage(driver);
  }

  public WelcomePage(WebDriver driver) {
    super(driver);

    String welcome = driver.findElement(By.tagName("h2")).getText();
    assertEquals("All the shiny tickets are here!", welcome);
  }

}
