package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class Pegi {

  @Json(name = "rating")
  private String rating;

  @Json(name = "synopsis")
  private String synopsis;

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  @Override
  public String toString() {
    return
        "Pegi{" +
            "rating = '" + rating + '\'' +
            ",synopsis = '" + synopsis + '\'' +
            "}";
  }
}
