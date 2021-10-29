package com.ticketmagpie;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
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
    apiClient = new ApiClient(TestEnvironment.server("127.0.0.1", port));
    xmlAssert = new XmlAssert();
  }

  @Test
  public void anXmlListOfAllConcerts() {
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
  public void anXmlSingleConcert() {
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
  public void newXmlConcert() {
    String sendXml = "" //
        + "<concert>" //
        + "  <band>Phoenix</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Phoenix is an indie pop band from Versailles, France.</description>" //
        + "  <image>image</image>" //
        + "</concert>";

    ResponseEntity<String> response = apiClient.postXml("/api/concerts.xml", sendXml);
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
  public void newXmlConcertWithXmlEntities() {
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

    ResponseEntity<String> response = apiClient.postXml("/api/concerts.xml", sendXml);
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

  @Test(timeout = 5000)
  @Ignore("Slow test execution")
  public void newXmlConcertWithBillionLaughs() {
    String sendXml = "" //
        + "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" //
        + "<!DOCTYPE concert [\n" //
        + "<!ENTITY lol \"lol\">\n" //
        + "<!ELEMENT lolz (#PCDATA)>\n" //
        + "<!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" //
        + "<!ENTITY lol2 \"&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;\">\n" //
        + "<!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\">\n" //
        + "<!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\">\n" //
        + "<!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\">\n" //
        + "<!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\">\n" //
        + "<!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\">\n" //
        + "<!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\">\n" //
        + "<!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\">\n" //
        + "]>\n" //
        + "<concert>\n" //
        + "<band>dos</band>\n" //
        + "<date>December 18th</date>\n" //
        + "<description>s &lol9; end</description>\n" //
        + "</concert>\n";

    ResponseEntity<String> response = apiClient.postXml("/api/concerts.xml", sendXml);
    assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void newXmlConcertWithHall() {
    String sendXml = "" //
        + "<concert>" //
        + "  <band>Phoenix</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Phoenix is an indie pop band from Versailles, France.</description>" //
        + "  <image>image</image>" //
        + "  <hall><location>Royal Albert Hall</location></hall>" //
        + "</concert>";

    ResponseEntity<String> response = apiClient.postXml("/api/concerts.xml", sendXml);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    lastId++;
  }
  
  @Test
  public void aJsonSingleConcert() {
    ResponseEntity<String> response = apiClient.get("/api/concerts/0.json");
    assertEquals(HttpStatus.OK, response.getStatusCode());

    String expectedJson = "" //
        + "{" //
        + "\"id\":0," //
        + "\"band\":\"Lake Malawi\"," //
        + "\"date\":\"July 15th\"," //
        + "\"description\":\"Lake Malawi is an indie-pop band formed by Albert Cerny in September 2013, based in UK and the Czech Republic.\"," //
        + "\"image\":\"Lake_Malawi_music_band.jpg\"," //
        + "\"organiser\":null"
        + "}";
    assertEquals(expectedJson, response.getBody());
  }

  @Test
  public void newJsonConcert() {
    String sendJson = "" //
        + "{" //
        + "  \"band\": \"Phoenix2\"," //
        + "  \"date\": \"December 19th\"," //
        + "  \"description\": \"Phoenix is an indie pop band from Versailles, France.\"," //
        + "  \"image\": \"image\"" //
        + "}";

    ResponseEntity<String> response = apiClient.postJson("/api/concerts.json", sendJson);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    String expectedJson = "" //
        + "{" //
        + "\"id\":" + (++lastId) + "," //
        + "\"band\":\"Phoenix2\"," //
        + "\"date\":\"December 19th\"," //
        + "\"description\":\"Phoenix is an indie pop band from Versailles, France.\"," //
        + "\"image\":\"image\"," //
        + "\"organiser\":null"
        + "}";
    assertEquals(expectedJson, response.getBody());
  }

  @Test
  public void newJsonConcertWithRadioStation() {
    String sendJson = "" //
        + "{" //
        + "  \"band\": \"Phoenix2\"," //
        + "  \"date\": \"December 19th\"," //
        + "  \"description\": \"Phoenix is an indie pop band from Versailles, France.\"," //
        + "  \"image\": \"image\"," //
        + "  \"organiser\":{\"type\":\"ConcertResource$RadioStation\",\"frequency\":\"109.8 FM\"}" //
        + "}";

    ResponseEntity<String> response = apiClient.postJson("/api/concerts.json", sendJson);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    lastId++;
  }
  
}
