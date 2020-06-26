package com.ticketmagpie.infrastructure;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration {
  // see https://stackoverflow.com/questions/48397996/spring-boot-enable-http-trace-method-for-embedded-tomcat

  @Bean
  public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
    factory.addConnectorCustomizers(connector -> {
      connector.setAllowTrace(true);
    });
    return factory;
  }
}
