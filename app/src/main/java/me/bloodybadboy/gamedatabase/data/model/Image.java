package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class Image {

  @Json(name = "width")
  private int width;

  @Json(name = "cloudinary_id")
  private String cloudinaryId;

  @Json(name = "url")
  private String url;

  @Json(name = "height")
  private int height;

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public String getCloudinaryId() {
    return cloudinaryId;
  }

  public void setCloudinaryId(String cloudinaryId) {
    this.cloudinaryId = cloudinaryId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public String toString() {
    return
        "Image{" +
            "width = '" + width + '\'' +
            ",cloudinary_id = '" + cloudinaryId + '\'' +
            ",url = '" + url + '\'' +
            ",height = '" + height + '\'' +
            "}";
  }
}