package com.ticketmagpie.pages;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

public class Page {

  protected final WebDriver driver;

  public Page(WebDriver driver) {
    this.driver = driver;
  }

  protected <T> void assertWaitingThat(Function<WebDriver, T> driverGetElement, Matcher<T> elementMatcher) {

    Function<WebDriver, Boolean> driverGetsElementAndMatches = new Function<WebDriver, Boolean>() {
      @Override
      public Boolean apply(WebDriver d) {
        return elementMatcher.matches(driverGetElement.apply(d));
      }

      @Override
      public String toString() {
        return elementMatcher.toString();
      }
    };

    WebDriverWait wait = new WebDriverWait(driver, 3, 250);
    wait.until(driverGetsElementAndMatches);
    assertThat(driverGetElement.apply(driver), elementMatcher);
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
