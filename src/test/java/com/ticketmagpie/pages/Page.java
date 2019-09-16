package com.ticketmagpie.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Page {

  protected final WebDriver driver;

  public Page(WebDriver driver) {
    this.driver = driver;
  }

  // general navigation

  public LoginPage clickLogin() {
    driver.findElement(By.partialLinkText("Log in")).click();
    return new LoginPage(driver);
  }

  public RegisterPage clickRegister() {
    driver.findElement(By.partialLinkText("Register")).click();
    return new RegisterPage(driver);
  }

  public UserPage clickUser(String username) {
    driver.findElement(By.partialLinkText(username)).click();

    return new UserPage(driver, username);
  }

  public LoginPage clickLogout() {
    driver.findElement(By.xpath("//input[@value='Sign Out']")).click();

    return new LoginPage(driver);
  }

}
