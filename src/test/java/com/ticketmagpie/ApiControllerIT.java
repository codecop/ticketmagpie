package com.ticketmagpie;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0", "spring.profiles.active=hsqldb" })
public class ApiControllerIT {

  @Value("${local.server.port}")
  private int port;
  private ApiClient apiClient;
  private XmlAssert xmlAssert;

  private static int lastId = 1;

  @Before
  public void prepareClient() {
    apiClient = new ApiClient("http://127.0.0.1:" + port);
    xmlAssert = new XmlAssert();
  }

  @Test
  public void alistOfAllConcerts() {
    ResponseEntity<String> response = apiClient.get("/api/concerts.xml");

    assertEquals(HttpStatus.OK, response.getStatusCode());

    String expectedXml = "" //
        + "<concerts>" //
        + "  <concert>" //
        + "    <id>0</id>" //
        + "    <band>Lake Malawi</band>" //
        + "    <date>July 15th</date>" //
        + "    <description>Lake Malawi is an indie-pop band formed by Albert Cerny in September 2013, based in UK and the Czech Republic.</description>" //
        + "    <image>Lake_Malawi_music_band.jpg</image>" //
        + "  </concert>" //
        + "  <concert>" //
        + "    <id>1</id>" //
        + "    <band>The Players</band>" //
        + "    <date>May 22nd</date>" //
        + "    <description>Re-discover The Players on their second tour at the City Concert Hall. Includes extraordinary new compositions and jazz classics.</description>" //
        + "    <image>jazz-band.jpg</image>" //
        + "  </concert>" //
        + "</concerts>";
    xmlAssert.bodyEquals(expectedXml, response);
  }

  @Test
  public void missingConcert() {
    ResponseEntity<String> response = apiClient.get("/api/concerts/99.xml");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void aSingleConcert() {
    ResponseEntity<String> response = apiClient.get("/api/concerts/0.xml");
    assertEquals(HttpStatus.OK, response.getStatusCode());

    String expectedXml = "" //
        + "<concert>" //
        + "  <id>0</id>" //
        + "  <band>Lake Malawi</band>" //
        + "  <date>July 15th</date>" //
        + "  <description>Lake Malawi is an indie-pop band formed by Albert Cerny in September 2013, based in UK and the Czech Republic.</description>" //
        + "  <image>Lake_Malawi_music_band.jpg</image>" //
        + "</concert>";
    xmlAssert.bodyEquals(expectedXml, response);
  }

  @Test
  public void newConcert() {
    String sendXml = "" //
        + "<concert>" //
        + "  <band>Phoenix</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Phoenix is an indie pop band from Versailles, France.</description>" //
        + "  <image>image</image>" //
        + "</concert>";

    ResponseEntity<String> response = apiClient.post("/api/concerts.xml", sendXml);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    String expectedXml = "" //
        + "<concert>" //
        + "  <id>" + (++lastId) + "</id>" //
        + "  <band>Phoenix</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Phoenix is an indie pop band from Versailles, France.</description>" //
        + "  <image>image</image>" //
        + "</concert>";
    xmlAssert.bodyEquals(expectedXml, response);
  }

  @Test
  public void newConcertWithXmlEntities() {
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

    ResponseEntity<String> response = apiClient.post("/api/concerts.xml", sendXml);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    String expectedXml = "" //
        + "<concert>" //
        + "  <id>" + (++lastId) + "</id>" //
        + "  <band>Phoeenixß</band>" //
        + "  <date>Decümber 18th</date>" //
        + "  <description>Phoeenix is an indie pop band from Versailles, France.</description>" //
        + "</concert>";
    xmlAssert.bodyEquals(expectedXml, response);
  }

}
