package com.ticketmagpie.infrastructure.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "concerts")
@ApiModel(value = "concerts")
public class ConcertsResource {

  // wrong name, so XML and Swagger are happy
  private List<ConcertResource> concert;

  public ConcertsResource() {
  }

  public ConcertsResource(List<ConcertResource> concerts) {
    this.concert = concerts;
  }

  public List<ConcertResource> getConcert() {
    return concert;
  }

  public void setConcert(List<ConcertResource> concerts) {
    this.concert = concerts;
  }

  @Override
  public String toString() {
    return "ConcertsResource{" + "concerts=" + concert + '}';
  }
}
