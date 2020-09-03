package com.ticketmagpie.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ApiSwaggerConfig {

  // see https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2). //
        select(). //
          apis(RequestHandlerSelectors.any()). //
          paths(PathSelectors.ant("/api/**")). //
        build(). //
        apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo("TicketMagpie REST API", // 
        "List and create concerts.", // 
        "0.2", //
        "", //
        new Contact("Daniel Billing and David Hatanian", //
                "https://thetestdoctor.co.uk/2016/10/11/introducing-ticket-magpie/", //
                "ticketmagpie.danbilling@yopmail.com"), //
        "CC-BY 3.0", //
        "http://html5up.net/license");
  }

}
