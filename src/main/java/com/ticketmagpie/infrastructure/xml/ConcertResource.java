package com.ticketmagpie.infrastructure.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "concert")
public class ConcertResource {

  private Integer id;
  private String band;
  private String date;
  private String description;

  public ConcertResource() {
  }

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

  public void setBand(String band) {
    this.band = band;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "ConcertEntity{" + "id='" + id + '\'' + ", band='" + band + '\'' + ", description='" + description + '\'' + '}';
  }
}
