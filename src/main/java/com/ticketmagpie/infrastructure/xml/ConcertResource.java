package com.ticketmagpie.infrastructure.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
  private Organiser organiser; // allow different content

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

  @XmlElementRefs({
    @XmlElementRef(type = RadioStation.class),
    @XmlElementRef(type = ConcertHall.class),
  })
  @ApiModelProperty(position = 5)
  public Organiser getOrganiser() {
    return organiser;
  }

  public void setOrganiser(Organiser organiser) {
    this.organiser = organiser;
  }
  
  @Override
  public String toString() {
    return "ConcertResource{" + "id='" + id + '\'' + ", band='" + band + '\'' + ", description='" + description + ", image='" + image + '\''
        + '}';
  }

  static abstract class Organiser {
    abstract String getName();
  }
  
  @XmlRootElement(name = "radio")
  @XmlType(propOrder = { "name" })
  @ApiModel(value = "radio")
  static class RadioStation extends Organiser {
    String name;

    @Override
    @XmlElement
    @ApiModelProperty(position = 1)
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
    
  }

  @XmlRootElement(name = "hall")
  @XmlType(propOrder = { "name" })
  @ApiModel(value = "hall")
  static class ConcertHall extends Organiser {
    String name;
    
    @Override
    @XmlElement
    @ApiModelProperty(position = 1)
    public String getName() {
      return name;
    }
    
    public void setName(String name) {
      this.name = name;
    }
    
  }

}
