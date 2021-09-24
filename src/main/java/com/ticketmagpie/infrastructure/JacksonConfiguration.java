package com.ticketmagpie.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

@Configuration
public class JacksonConfiguration {
  // see https://www.geeksforgeeks.org/spring-boot-customize-the-jackson-objectmapper/

  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    ObjectMapper.DefaultTypeResolverBuilder typeResolverBuilder = new ObjectMapper.DefaultTypeResolverBuilder(
        DefaultTyping.OBJECT_AND_NON_CONCRETE);

    // public ObjectMapper enableDefaultTyping() {
    //   return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
    // }
    System.out.println("CALLED XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

    return new Jackson2ObjectMapperBuilder(). //
        defaultTyping(typeResolverBuilder);
  }
}
