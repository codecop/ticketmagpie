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
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;
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
  private static int lastId = 1;

  @Before
  public void prepareTemplate() {
    apiBaseUrl = "http://127.0.0.1:" + port;
    template = new TestRestTemplate();
  }

  @Test
  public void alistOfAllConcerts() {
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

    ResponseEntity<String> response = getApi("/api/concerts.xml");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertXmlBodyEquals(expectedXml, response);
  }

  private void assertXmlBodyEquals(String expectedXml, ResponseEntity<String> response) {
    String actualXml = response.getBody();
    assertThat(actualXml, CompareMatcher.isSimilarTo(expectedXml). //
        withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText)). //
        ignoreWhitespace(). //
        ignoreComments());
  }

  @Test
  public void aSingleConcert() {
    ResponseEntity<String> noResponse = getApi("/api/concerts/99.xml");
    assertEquals(HttpStatus.NOT_FOUND, noResponse.getStatusCode());

    String expectedXml = "" //
        + "<concert>" //
        + "  <id>0</id>" //
        + "  <band>Lake Malawi</band>" //
        + "  <date>July 15th</date>" //
        + "  <description>Lake Malawi is an indie-pop band formed by Albert Cerny in September 2013, based in UK and the Czech Republic.</description>" //
        + "</concert>";

    ResponseEntity<String> response = getApi("/api/concerts/0.xml");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertXmlBodyEquals(expectedXml, response);
  }

  private ResponseEntity<String> getApi(String path) {
    // see https://stackoverflow.com/a/21637522/104143
    return template.exchange(apiBaseUrl + path, HttpMethod.GET, acceptXml(), String.class);
  }

  private HttpEntity<String> acceptXml() {
    // Set XML content type explicitly to force response in XML (If not spring gets response in JSON)
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    return new HttpEntity<>(headers);
  }

  @Test
  public void newConcert() {
    String sendXml = "" //
        + "<concert>" //
        + "  <band>Phoenix</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Phoenix is an indie pop band from Versailles, France.</description>" //
        + "</concert>";

    ResponseEntity<String> postResponse = postApi("/api/concerts.xml", sendXml);
    assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

    String expectedXml = "" //
        + "<concert>" //
        + "  <id>" + (++lastId) + "</id>" //
        + "  <band>Phoenix</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Phoenix is an indie pop band from Versailles, France.</description>" //
        + "</concert>";

    assertXmlBodyEquals(expectedXml, postResponse);
  }

  @Test
  public void zNewConcertWithXmlEntities() {
    String sendXml = "" //
        + "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" // 
        + "<!DOCTYPE concert [\n" //
        + "<!ENTITY ouml \"oe\" >\n" // plain entity 
        + "<!ENTITY uuml \"&#252;\" >\n" // numeric entity
        + "]>" //
        + "<concert>\n" //
        + "  <band>Phoeenixß</band>\n" // ISO-8859-1 character
        + "  <date>Dec&uuml;mber 18th</date>\n" //
        + "  <description>Ph&ouml;enix is an indie pop band from Versailles, France.</description>\n" //
        + "</concert>\n";

    ResponseEntity<String> postResponse = postApi("/api/concerts.xml", sendXml);
    assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

    String expectedXml = "" //
        + "<concert>" //
        + "  <id>" + (++lastId) + "</id>" //
        + "  <band>Phoeenixß</band>" //
        + "  <date>Decümber 18th</date>" //
        + "  <description>Phoeenix is an indie pop band from Versailles, France.</description>" //
        + "</concert>";

    System.out.println(postResponse.getBody());
    assertXmlBodyEquals(expectedXml, postResponse);
  }

  private ResponseEntity<String> postApi(String path, String sendXml) {
    return template.exchange(apiBaseUrl + path, HttpMethod.POST, sendAndAcceptXml(sendXml), String.class);
  }

  private HttpEntity<String> sendAndAcceptXml(String sendXml) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    headers.setContentType(MediaType.APPLICATION_XML);
    return new HttpEntity<>(sendXml, headers);
  }

}
