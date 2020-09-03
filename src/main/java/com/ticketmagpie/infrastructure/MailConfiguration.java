package com.ticketmagpie.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail.smtp")
public class MailConfiguration {
  // see https://stackoverflow.com/a/35535138

  private boolean auth;
  private boolean tls;
  private String host;
  private int port;
  private String username;
  private String password;

  public boolean isAuth() {
    return auth;
  }

  public void setAuth(boolean auth) {
    this.auth = auth;
  }

  public boolean isTls() {
    return tls;
  }

  public void setTls(boolean tls) {
    this.tls = tls;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
