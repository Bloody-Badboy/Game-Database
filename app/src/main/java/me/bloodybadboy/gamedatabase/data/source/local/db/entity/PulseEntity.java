package me.bloodybadboy.gamedatabase.data.source.local.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_pulses")
public class PulseEntity {

  @PrimaryKey
  @ColumnInfo(name = "pulse_id")
  private long id;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "published_at")
  private long publishedAt;

  @ColumnInfo(name = "author")
  private String author;

  @ColumnInfo(name = "image_cloudinary_id")
  private String imageCloudinaryId;

  @ColumnInfo(name = "url")
  private String url;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public long getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(long publishedAt) {
    this.publishedAt = publishedAt;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getImageCloudinaryId() {
    return imageCloudinaryId;
  }

  public void setImageCloudinaryId(String imageCloudinaryId) {
    this.imageCloudinaryId = imageCloudinaryId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
