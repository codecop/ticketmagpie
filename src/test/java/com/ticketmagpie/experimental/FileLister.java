package com.ticketmagpie.experimental;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ticketmagpie.ApiClient;

public class FileLister {

  private ApiClient apiClient = new ApiClient("http://127.0.0.1:8080");

  public int createWithImage(String image) throws IOException {
    String sendXml = "" //
        + "<concert>" //
        + "  <band>Band for " + image + "</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Some description.</description>" //
        + "  <image>" + image + "</image>" //
        + "</concert>";

    ResponseEntity<String> response = apiClient.post("/api/concerts.xml", sendXml);
    if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
      throw new IOException(response.getStatusCode().getReasonPhrase());
    }

    return idFrom(response.getBody());
  }

  private int idFrom(String xml) {
    int s = xml.indexOf("<id>") + 4;
    int e = xml.indexOf("</id>", s);
    return Integer.parseInt(xml.substring(s, e));
  }

  public String readImage(int id) throws IOException {
    // TODO download image
    ResponseEntity<String> response = apiClient.get("/concertimage?id=" + id);
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      throw new IOException(response.getStatusCode().getReasonPhrase());
    }

    return response.getBody();
  }

}
