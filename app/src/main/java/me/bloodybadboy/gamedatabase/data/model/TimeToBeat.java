package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;

public class TimeToBeat {
  @Json(name = "hastly")
  private int hastly;

  @Json(name = "completely")
  private int completely;

  @Json(name = "normally")
  private int normally;

  public int getHastly() {
    return hastly;
  }

  public void setHastly(int hastly) {
    this.hastly = hastly;
  }

  public int getCompletely() {
    return completely;
  }

  public void setCompletely(int completely) {
    this.completely = completely;
  }

  public int getNormally() {
    return normally;
  }

  public void setNormally(int normally) {
    this.normally = normally;
  }

  @Override
  public String toString() {
    return
        "TimeToBeat{" +
            "hastly = '" + hastly + '\'' +
            ",completely = '" + completely + '\'' +
            ",normally = '" + normally + '\'' +
            "}";
  }
}
