package com.ticketmagpie.infrastructure.xml;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class EnableEntityResolutionWebConfig extends WebMvcConfigurerAdapter {

  // see https://www.baeldung.com/spring-httpmessageconverter-rest

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof Jaxb2RootElementHttpMessageConverter) {
        Jaxb2RootElementHttpMessageConverter jaxbConverter = (Jaxb2RootElementHttpMessageConverter) converter;
        jaxbConverter.setProcessExternalEntities(true);
      }
    }
  }

}