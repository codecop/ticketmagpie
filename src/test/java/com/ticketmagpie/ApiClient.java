package com.ticketmagpie;

import java.util.Arrays;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Communicate with the API.
 */
class ApiClient {

  private final String baseUrl;
  private final RestTemplate template;

  public ApiClient(String baseUrl) {
    this.baseUrl = baseUrl;
    this.template = new TestRestTemplate();
  }

  public ResponseEntity<String> get(String path) {
    // see https://stackoverflow.com/a/21637522/104143
    String url = baseUrl + path;
    HttpEntity<String> request = acceptXml();
    return template.exchange(url, HttpMethod.GET, request, String.class);
  }

  private HttpEntity<String> acceptXml() {
    HttpHeaders headers = acceptXmlHeader();
    return new HttpEntity<>(headers);
  }

  private HttpHeaders acceptXmlHeader() {
    // Set XML content type explicitly to force response in XML (If not spring sends response in JSON)
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    return headers;
  }

  public ResponseEntity<String> post(String path, String sendXml) {
    String url = baseUrl + path;
    HttpEntity<String> request = sendAndAcceptXml(sendXml);
    return template.exchange(url, HttpMethod.POST, request, String.class);
  }

  private HttpEntity<String> sendAndAcceptXml(String sendXml) {
    HttpHeaders headers = acceptXmlHeader();
    headers.setContentType(MediaType.APPLICATION_XML);
    return new HttpEntity<>(sendXml, headers);
  }

}
