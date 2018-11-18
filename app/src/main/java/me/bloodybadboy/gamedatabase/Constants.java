package me.bloodybadboy.gamedatabase;

public final class Constants {
  public static final String PACKAGE = "me.bloodybadboy.gamedatabase";

  public static final String API_SERVICE_ENDPOINT = "https://api-endpoint.igdb.com";
  public static final String IMAGE_URL_FORMAT =
      "https://images.igdb.com/igdb/image/upload/t_%s/%s.jpg";

  public static final String COVER_SIZE = "cover_big";
  public static final String COVER_SIZE_SEARCH = "cover_small";
  public static final String SCREENSHOT_SIZE_MID = "screenshot_med";
  public static final String SCREENSHOT_SIZE_HUGE = "screenshot_huge";
  public static final String IMAGE_THUMB = "thumb";

  public static final int THEME_EROTIC_ID = 42;

  public static final int GAME_LIST_LIMIT = 20;
  public static final int PULSE_LIST_LIMIT = 15;
  public static final int OFFLINE_PULSE_LIST_LIMIT = 3;

  public static final String YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
  public static final String YOUTUBE_THUMBNAIL_IMAGE_SIZE = "mqdefault.jpg";

  public enum GameListFilterType {
    POPULARITY,
    TOP_RATED,
    COMING_SOON,
    FAVOURITES
  }
}
