package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class Pulse {

  @Json(name = "summary")
  private String summary;

  @Json(name = "uid")
  private String uid;

  @Json(name = "author")
  private String author;

  @Json(name = "id")
  private long id;

  @Json(name = "title")
  private String title;

  @Json(name = "published_at")
  private long publishedAt;

  @Json(name = "url")
  private String url;

  @Json(name = "pulse_source")
  private int pulseSource;

  @Json(name = "pulse_image")
  private Image pulseImage;

  public Image getPulseImage() {
    return pulseImage;
  }

  public void setPulseImage(Image pulseImage) {
    this.pulseImage = pulseImage;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getPulseSource() {
    return pulseSource;
  }

  public void setPulseSource(int pulseSource) {
    this.pulseSource = pulseSource;
  }

  @Override
  public String toString() {
    return
        "Pulse{" +
            "summary = '" + summary + '\'' +
            ",uid = '" + uid + '\'' +
            ",author = '" + author + '\'' +
            ",id = '" + id + '\'' +
            ",title = '" + title + '\'' +
            ",published_at = '" + publishedAt + '\'' +
            ",url = '" + url + '\'' +
            ",pulse_source = '" + pulseSource + '\'' +
            "}";
  }
}