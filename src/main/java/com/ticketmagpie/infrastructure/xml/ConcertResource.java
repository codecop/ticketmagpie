package com.ticketmagpie.infrastructure.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "concert")
public class ConcertResource {

  private Integer id;
  private String band;
  private String date;
  private String description;

  public ConcertResource(Integer id, String band, String date, String description) {
    this.id = id;
    this.band = band;
    this.date = date;
    this.description = description;
  }

  public Integer getId() {
    return id;
  }

  public String getBand() {
    return band;
  }

  public String getDescription() {
    return description;
  }

  public String getDate() {
    return date;
  }

  @Override
  public String toString() {
    return "ConcertEntity{" + "id='" + id + '\'' + ", band='" + band + '\'' + ", description='" + description + '\'' + '}';
  }
}
