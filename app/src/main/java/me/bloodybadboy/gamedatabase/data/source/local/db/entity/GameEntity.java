package me.bloodybadboy.gamedatabase.data.source.local.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.squareup.moshi.Json;

@Entity(tableName = "favourite_games")
public class GameEntity {
  @PrimaryKey
  @Json(name = "game_id")
  private long id;

  @ColumnInfo(name = "game_name")
  private String name;

  @ColumnInfo(name = "cover_cloudinary_id")
  private String coverCloudinaryId;

  @ColumnInfo(name = "first_release_date")
  private long firstReleaseDate;

  @ColumnInfo(name = "rating")
  private double rating;

  @ColumnInfo(name = "esrb_rating")
  private String esrbRating;

  @ColumnInfo(name = "themes_json")
  private String themesJson;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCoverCloudinaryId() {
    return coverCloudinaryId;
  }

  public void setCoverCloudinaryId(String coverCloudinaryId) {
    this.coverCloudinaryId = coverCloudinaryId;
  }

  public long getFirstReleaseDate() {
    return firstReleaseDate;
  }

  public void setFirstReleaseDate(long firstReleaseDate) {
    this.firstReleaseDate = firstReleaseDate;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getEsrbRating() {
    return esrbRating;
  }

  public void setEsrbRating(String esrbRating) {
    this.esrbRating = esrbRating;
  }

  public String getThemesJson() {
    return themesJson;
  }

  public void setThemesJson(String themesJson) {
    this.themesJson = themesJson;
  }
}
