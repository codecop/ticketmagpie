package com.ticketmagpie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.xmlunit.matchers.CompareMatcher;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0", "spring.profiles.active=hsqldb" })
public class ApiControllerIT {

  @Value("${local.server.port}")
  private int port;

  private String apiBaseUrl;
  private RestTemplate template;

  @Before
  public void prepareTemplate() {
    apiBaseUrl = "http://127.0.0.1:" + port;
    template = new TestRestTemplate();
  }

  @Test
  public void listAllConcerts() {
    String expectedXml = "" //
        + "<concerts>" //
        + "  <concert>" //
        + "    <id>0</id>" //
        + "    <band>Lake Malawi</band>" //
        + "    <date>July 15th</date>" //
        + "    <description>Lake Malawi is an indie-pop band formed by Albert Cerny in September 2013, based in UK and the Czech Republic.</description>" //
        + "  </concert>" //
        + "  <concert>" //
        + "    <id>1</id>" //
        + "    <band>The Players</band>" //
        + "    <date>May 22nd</date>" //
        + "    <description>Re-discover The Players on their second tour at the City Concert Hall. Includes extraordinary new compositions and jazz classics.</description>" //
        + "  </concert>" //
        + "</concerts>";
    ResponseEntity<String> response = requestApi("/api/concerts.xml");
    assertXmlBodyEquals(expectedXml, response);
  }

  @Test
  public void getConcert() {
    ResponseEntity<String> noResponse = requestApi("/api/concerts/3.xml");
    assertEquals(HttpStatus.NOT_FOUND, noResponse.getStatusCode());

    String expectedXml = "" //
        + "<concert>" //
        + "  <id>0</id>" //
        + "  <band>Lake Malawi</band>" //
        + "  <date>July 15th</date>" //
        + "  <description>Lake Malawi is an indie-pop band formed by Albert Cerny in September 2013, based in UK and the Czech Republic.</description>" //
        + "</concert>";
    ResponseEntity<String> response = requestApi("/api/concerts/0.xml");
    assertXmlBodyEquals(expectedXml, response);
  }

  private ResponseEntity<String> requestApi(String path) {
    // see https://stackoverflow.com/a/21637522/104143

    // Set XML content type explicitly to force response in XML (If not spring gets response in JSON)
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

    return template.exchange(apiBaseUrl + path, HttpMethod.GET, entity, String.class);
  }

  private void assertXmlBodyEquals(String expectedXml, ResponseEntity<String> actualResponse) {
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());

    String actualXml = actualResponse.getBody();
    assertThat(actualXml, CompareMatcher.isIdenticalTo(expectedXml).ignoreWhitespace().ignoreComments());
  }
}
