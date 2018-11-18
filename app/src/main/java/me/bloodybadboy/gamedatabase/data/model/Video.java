package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class Video {

  @Json(name = "name")
  private String name;

  @Json(name = "video_id")
  private String videoId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVideoId() {
    return videoId;
  }

  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }

  @Override
  public String toString() {
    return
        "Video{" +
            "name = '" + name + '\'' +
            ",video_id = '" + videoId + '\'' +
            "}";
  }
}