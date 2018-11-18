package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;
import java.util.List;

public class Theme {

  @Json(name = "updated_at")
  private int updatedAt;

  @Json(name = "name")
  private String name;

  @Json(name = "games")
  private List<Integer> games;

  @Json(name = "created_at")
  private int createdAt;

  @Json(name = "id")
  private int id;

  @Json(name = "slug")
  private String slug;

  @Json(name = "url")
  private String url;

  public int getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(int updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Integer> getGames() {
    return games;
  }

  public void setGames(List<Integer> games) {
    this.games = games;
  }

  public int getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(int createdAt) {
    this.createdAt = createdAt;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
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
        "Theme{" +
            "updated_at = '" + updatedAt + '\'' +
            ",name = '" + name + '\'' +
            ",games = '" + games + '\'' +
            ",created_at = '" + createdAt + '\'' +
            ",id = '" + id + '\'' +
            ",slug = '" + slug + '\'' +
            ",url = '" + url + '\'' +
            "}";
  }
}