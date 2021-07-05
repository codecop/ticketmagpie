package com.ticketmagpie;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    Map<String, Object> properties = new HashMap<>();
    properties.put("server.port", "" + determinePort());
    app.setDefaultProperties(properties);
    app.run(args);
  }

  private static int determinePort() {
    // get the Heroku assigned port
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    // else get the test assigned port
    String port = System.getProperty("PORT", "8080"); // -DPORT=8080
    return Integer.parseInt(port);
  }

}
