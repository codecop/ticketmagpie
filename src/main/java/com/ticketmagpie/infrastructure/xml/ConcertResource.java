package com.ticketmagpie.infrastructure.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "concert")
@XmlType(propOrder = { "id", "band", "date", "description", "image", "organiser" })
@ApiModel(value = "concert")
public class ConcertResource {

  private Integer id;
  private String band;
  private String date;
  private String description;
  private String image;
  private Object organiser; // allow different organisers

  public ConcertResource() {
  }

  public ConcertResource(Integer id, String band, String date, String description, String image) {
    this.id = id;
    this.band = band;
    this.date = date;
    this.description = description;
    this.image = image;
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

  @XmlElement
  @ApiModelProperty(position = 4)
  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @XmlElementRefs({ //
      @XmlElementRef(type = RadioStation.class), //
      @XmlElementRef(type = ConcertHall.class), })
  @ApiModelProperty(position = 5)
  // see https://www.geeksforgeeks.org/spring-boot-customize-the-jackson-objectmapper/ ... does not work
  // also spring.jackson.default-typing.object-and-non-concrete=true ... does not work
  // see https://www.baeldung.com/jackson-inheritance
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes({ @Type(value = RadioStation.class), @Type(value = ConcertHall.class) })
  public Object getOrganiser() {
    return organiser;
  }

  public void setOrganiser(Object organiser) {
    this.organiser = organiser;
  }

  @Override
  public String toString() {
    return "ConcertResource{" + "id='" + id + '\'' + ", band='" + band + '\'' + ", description='" + description + ", image='" + image + '\''
        + '}';
  }

  @XmlRootElement(name = "radio")
  @ApiModel(value = "radio")
  public static class RadioStation {
    private String frequency;

    public RadioStation() {
    }

    public RadioStation(String frequency) {
      this.frequency = frequency;
    }

    @XmlElement
    public String getFrequency() {
      return frequency;
    }

    public void setFrequency(String frequency) {
      this.frequency = frequency;
    }

    @Override
    public String toString() {
      return "RadioStation at " + frequency;
    }

  }

  @XmlRootElement(name = "hall")
  @ApiModel(value = "hall")
  public static class ConcertHall {
    private String location;

    public ConcertHall() {
    }
    
    public ConcertHall(String location) {
      this.location = location;
    }

    @XmlElement
    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }

    @Override
    public String toString() {
      return "ConcertHall at " + location;
    }
  }

}
