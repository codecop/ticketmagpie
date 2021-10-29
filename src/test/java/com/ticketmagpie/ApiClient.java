package com.ticketmagpie;

import java.util.Collections;

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
public class ApiClient {

  private final String baseUrl;
  private final RestTemplate template;

  public ApiClient(String baseUrl) {
    this.baseUrl = baseUrl;
    this.template = new TestRestTemplate();
  }

  public ResponseEntity<String> get(String path) {
    // see https://stackoverflow.com/a/21637522/104143
    String url = baseUrl + path;
    HttpEntity<String> request = acceptFromFile(path);
    return template.exchange(url, HttpMethod.GET, request, String.class);
  }

  private HttpEntity<String> acceptFromFile(String path) {
    HttpHeaders headers = acceptHeaderFromFile(path);
    return new HttpEntity<>(headers);
  }

  private HttpHeaders acceptHeaderFromFile(String path) {
    HttpHeaders headers = new HttpHeaders();
    if (path.toLowerCase().endsWith(".xml")) {
      // Set XML content type explicitly to force response in XML (If not spring sends response in JSON)
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
    } else if (path.toLowerCase().endsWith(".json")) {
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }
    return headers;
  }

  public ResponseEntity<String> postXml(String path, String sendXml) {
    return post(path, MediaType.APPLICATION_XML, sendXml);
  }

  public ResponseEntity<String> postJson(String path, String sendJson) {
    return post(path, MediaType.APPLICATION_JSON, sendJson);
  }

  private ResponseEntity<String> post(String path, MediaType mediaType, String data) {
    String url = baseUrl + path;
    HttpEntity<String> request = sendAndAccept(data, mediaType);
    return template.exchange(url, HttpMethod.POST, request, String.class);
  }

  private HttpEntity<String> sendAndAccept(String sendJson, MediaType mediaType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(mediaType));
    headers.setContentType(mediaType);
    return new HttpEntity<>(sendJson, headers);
  }

}
