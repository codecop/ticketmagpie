package com.ticketmagpie;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0", "spring.profiles.active=hsqldb" })
public class ApiControllerIT {

  @Value("${local.server.port}")
  private int port;

  private URL base;
  private RestTemplate template;

  @Before
  public void setUp() throws Exception {
    this.base = new URL("http://localhost:" + port + "/concerts.xml");
    template = new TestRestTemplate();
  }

  @Test
  public void getTicketMagpie() {
    ResponseEntity<List> response = template.getForEntity(base.toString(), List.class);
    assertEquals(2, response.getBody().size());
  }
}
