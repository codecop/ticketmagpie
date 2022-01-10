package com.ticketmagpie;

public class Concert {
  private Integer id;
  private String band;
  private String date;
  private String description;
  private String imageUrl;
  private byte[] imageBlob;
  private String organiser;

  public Concert(Integer id, String band, String date, String description, String imageUrl, byte[] imageBlob, String organiser) {
    this.id = id;
    this.band = band;
    this.date = date;
    this.description = description;
    this.imageUrl = imageUrl;
    this.imageBlob = imageBlob;
    this.organiser = organiser;
  }

  public String getBand() {
    return band;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getDate() {
    return date;
  }

  public Integer getId() { return id; }

  public byte[] getImageBlob() {
    return imageBlob;
  }

  public boolean getHasImage() {
    return imageUrl != null ||
        (imageBlob != null && imageBlob.length > 0);
  }
  
  public String getOrganiser() {
    return organiser;
  }
  
  @Override
  public String toString() {
    return "Concert{" +
        "id='" + id + '\'' +
        ", band='" + band + '\'' +
        ", description='" + description + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", organiser='" + organiser + '\'' +
        '}';
  }
}
