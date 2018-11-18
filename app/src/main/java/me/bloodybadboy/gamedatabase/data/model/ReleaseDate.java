package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class ReleaseDate {

  @Json(name = "game")
  private int game;

  @Json(name = "id")
  private int id;

  @Json(name = "category")
  private int category;

  @Json(name = "human")
  private String human;

  @Json(name = "platform")
  private int platform;

  public int getGame() {
    return game;
  }

  public void setGame(int game) {
    this.game = game;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public String getHuman() {
    return human;
  }

  public void setHuman(String human) {
    this.human = human;
  }

  public int getPlatform() {
    return platform;
  }

  public void setPlatform(int platform) {
    this.platform = platform;
  }

  @Override
  public String toString() {
    return
        "ReleaseDate{" +
            "game = '" + game + '\'' +
            ",id = '" + id + '\'' +
            ",category = '" + category + '\'' +
            ",human = '" + human + '\'' +
            ",platform = '" + platform + '\'' +
            "}";
  }
}