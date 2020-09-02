package com.ticketmagpie.pages;

import static org.hamcrest.Matchers.containsString;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ForgetPasswordPage extends Page {

  public ForgetPasswordPage(WebDriver driver) {
    super(driver);

    assertWaitingThat(WebDriver::getPageSource, containsString("Enter your username to recover your password"));
  }

  public MailSentPage submit(String username) {
    driver.findElement(By.xpath("//input[@name='user']")).sendKeys(username);
    driver.findElement(By.xpath("//input[@type='submit']")).click();

    return new MailSentPage(driver);
  }

}
