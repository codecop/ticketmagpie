package com.ticketmagpie.infrastructure.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "concert")
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

  @XmlElement(name = "id")
  public Integer getId() {
    return id;
  }

  @XmlElement(name = "band")
  public String getBand() {
    return band;
  }

  public void setBand(String band) {
    this.band = band;
  }

  @XmlElement(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlElement(name = "date")
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "ConcertResource{" + "id='" + id + '\'' + ", band='" + band + '\'' + ", description='" + description + '\'' + '}';
  }
}
