package com.ticketmagpie.pages;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends Page {

  public LoginPage(WebDriver driver) {
    super(driver);

    assertThat(driver.getPageSource(), containsString("Please enter your user name and password"));
    assertThat(driver.getPageSource(), not(containsString("junit")));
  }

  public ForgetPasswordPage clickForgot() {
    driver.findElement(By.partialLinkText("Forgot your password?")).click();
    return new ForgetPasswordPage(driver);
  }

  public WelcomePage login(String username, String password) {
    performLogin(username, password);

    return new WelcomePage(driver);
  }

  private void performLogin(String username, String password) {
    driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
    driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
    driver.findElement(By.xpath("//input[@type='submit']")).click();
  }

  public void wrongLogin(String username, String password) {
    performLogin(username, password);

    assertThat(driver.getPageSource(), containsString("Unable to log in"));
    assertThat(driver.getPageSource(), not(containsString("junit")));
  }

}
