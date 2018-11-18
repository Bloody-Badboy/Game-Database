package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class Website {
  @Json(name = "category")
  private int category;

  @Json(name = "url")
  private String url;

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return
        "Website{" +
            "category = '" + category + '\'' +
            ",url = '" + url + '\'' +
            "}";
  }
}
