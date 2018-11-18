package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class Company {

  @Json(name = "name")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return
        "Company{" +
            "name = '" + name + '\'' +
            "}";
  }
}