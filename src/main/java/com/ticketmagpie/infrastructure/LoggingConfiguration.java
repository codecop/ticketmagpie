package com.ticketmagpie.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

  @Bean
  public Logger log() {
    return LoggerFactory.getLogger("com.ticketmagpie");
  }
}
