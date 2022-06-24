package com.ticketmagpie.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionPoint.Category;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.User;
import org.owasp.appsensor.core.event.EventManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private TicketMagPieAuthenticationProvider ticketMagPieAuthenticationProvider;

  @Autowired
  private TicketMagPieRememberMeService ticketMagPieRememberMeService;

  @Autowired 
  private AppSensorClient appSensorClient;
  
  @Autowired 
  private EventManager ids;
    
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        //Necessary for HTML forms to work
        .csrf().disable()
        //Required if we ever want to include the app in an iframe
        .headers().disable()
        .authorizeRequests()
        .antMatchers("/user/**").authenticated()
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .logout()
        .addLogoutHandler((req, res, auth) -> {
          DetectionPoint detectionPoint = new DetectionPoint(Category.AUTHENTICATION, "ASR-J");
          String userName = String.valueOf(auth.getPrincipal());
          User user = new User(userName);
          ids.addEvent(new Event(user, detectionPoint, getDetectionSystem()));
          System.out.println("LOGOUT!!!!");
        })
        .permitAll()
        .and()
        .rememberMe()
        .rememberMeServices(ticketMagPieRememberMeService)
    ;
  }

  private DetectionSystem getDetectionSystem() {
    return new DetectionSystem(
        appSensorClient.getConfiguration().getServerConnection().getClientApplicationIdentificationHeaderValue());
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth
        .authenticationProvider(ticketMagPieAuthenticationProvider)
        .eraseCredentials(false);
  }

  @Bean
  public SpringSecurityDialect securityDialect() {
    return new SpringSecurityDialect();
  }
}
