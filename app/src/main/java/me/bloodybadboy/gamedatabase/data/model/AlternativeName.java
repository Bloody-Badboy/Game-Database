package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class AlternativeName {
  @Json(name = "name")
  private String name;

  @Json(name = "comment")
  private String comment;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return
        "AlternativeName{" +
            "name = '" + name + '\'' +
            ",comment = '" + comment + '\'' +
            "}";
  }
}
