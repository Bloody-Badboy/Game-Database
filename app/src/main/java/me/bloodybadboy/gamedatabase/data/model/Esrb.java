package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class Esrb {
  @Json(name = "rating")
  private String rating;

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  @Override
  public String toString() {
    return
        "Esrb{" +
            "rating = '" + rating + '\'' +
            "}";
  }
}
