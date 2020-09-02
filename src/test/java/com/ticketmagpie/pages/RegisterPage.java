package com.ticketmagpie.pages;

import static org.hamcrest.Matchers.containsString;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends Page {

  public RegisterPage(WebDriver driver) {
    super(driver);

    assertWaitingThat(WebDriver::getPageSource, containsString("Please enter the user name and password you would like"));
  }

  public LoginPage register(String username, String password) {
    driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
    driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
    driver.findElement(By.xpath("//input[@type='submit']")).click();

    return new LoginPage(driver);
  }

}
