package com.ticketmagpie.infrastructure.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "concerts")
public class ConcertsResource {

  private List<ConcertResource> concerts;

  public ConcertsResource() {
  }

  public ConcertsResource(List<ConcertResource> concerts) {
    this.concerts = concerts;
  }

  @XmlElement(name = "concert")
  public List<ConcertResource> getConcerts() {
    return concerts;
  }

  public void setConcerts(List<ConcertResource> concerts) {
    this.concerts = concerts;
  }

  @Override
  public String toString() {
    return "ConcertsResource{" + "concerts=" + concerts + '}';
  }
}
