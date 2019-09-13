package com.ticketmagpie.infrastructure.xml;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "concerts")
public class ConcertsResource {

  private List<ConcertResource> concerts;

  public ConcertsResource(List<ConcertResource> concerts) {
    this.concerts = concerts;
  }

  @JacksonXmlProperty(localName = "concert")
  @JacksonXmlElementWrapper(useWrapping = false)
  public List<ConcertResource> getConcerts() {
    return concerts;
  }

  @Override
  public String toString() {
    return "ConcertsResource{" + "concerts=" + concerts + '}';
  }
}
