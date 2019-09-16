package com.ticketmagpie.infrastructure.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "concert")
@XmlType(propOrder = { "id", "band", "date", "description" })
@ApiModel(value = "concert")
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

  @XmlElement
  @ApiModelProperty(position = 0)
  public Integer getId() {
    return id;
  }

  @XmlElement
  @ApiModelProperty(position = 1)
  public String getBand() {
    return band;
  }

  public void setBand(String band) {
    this.band = band;
  }

  @XmlElement
  @ApiModelProperty(position = 2)
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @XmlElement
  @ApiModelProperty(position = 3)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "ConcertResource{" + "id='" + id + '\'' + ", band='" + band + '\'' + ", description='" + description + '\'' + '}';
  }
}
