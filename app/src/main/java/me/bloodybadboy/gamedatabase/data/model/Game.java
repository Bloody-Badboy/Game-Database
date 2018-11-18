package me.bloodybadboy.gamedatabase.data.model;

import com.squareup.moshi.Json;
import java.util.List;
import java.util.Objects;

public class Game {

  @Json(name = "id")
  private long id;

  @Json(name = "franchise")
  private List<Long> franchise;

  @Json(name = "game")
  private long game;

  @Json(name = "keywords")
  private List<Long> keywords;

  @Json(name = "developers")
  private List<Company> developers;

  @Json(name = "rating")
  private double rating;

  @Json(name = "created_at")
  private long createdAt;

  @Json(name = "videos")
  private List<Video> videos;

  @Json(name = "aggregated_rating_count")
  private int aggregatedRatingCount;

  @Json(name = "alternative_names")
  private List<AlternativeName> alternativeNames;

  @Json(name = "time_to_beat")
  private TimeToBeat timeToBeat;

  @Json(name = "player_perspectives")
  private List<Long> playerPerspectives;

  @Json(name = "screenshots")
  private List<Image> screenshots;

  @Json(name = "cover")
  private Image cover;

  @Json(name = "themes")
  private List<Theme> themes;

  @Json(name = "updated_at")
  private long updatedAt;

  @Json(name = "genres")
  private List<Long> genres;

  @Json(name = "first_release_date")
  private long firstReleaseDate;

  @Json(name = "storyline")
  private String storyline;

  @Json(name = "popularity")
  private double popularity;

  @Json(name = "release_dates")
  private List<ReleaseDate> releaseDates;

  @Json(name = "games")
  private List<Long> games;

  @Json(name = "publishers")
  private List<Company> publishers;

  @Json(name = "total_rating")
  private double totalRating;

  @Json(name = "slug")
  private String slug;

  @Json(name = "hypes")
  private int hypes;

  @Json(name = "summary")
  private String summary;

  @Json(name = "game_modes")
  private List<Long> gameModes;

  @Json(name = "pegi")
  private Pegi pegi;

  @Json(name = "collection")
  private long collection;

  @Json(name = "url")
  private String url;

  @Json(name = "rating_count")
  private int ratingCount;

  @Json(name = "tags")
  private List<Integer> tags;

  @Json(name = "esrb")
  private Esrb esrb;

  @Json(name = "name")
  private String name;

  @Json(name = "total_rating_count")
  private int totalRatingCount;

  @Json(name = "aggregated_rating")
  private double aggregatedRating;

  @Json(name = "game_engines")
  private List<Long> gameEngines;

  @Json(name = "websites")
  private List<Website> websites;

  @Json(name = "category")
  private int category;

  @Json(name = "status")
  private int status;

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Game game1 = (Game) o;
    return id == game1.id &&
        game == game1.game &&
        Double.compare(game1.rating, rating) == 0 &&
        createdAt == game1.createdAt &&
        aggregatedRatingCount == game1.aggregatedRatingCount &&
        updatedAt == game1.updatedAt &&
        firstReleaseDate == game1.firstReleaseDate &&
        Double.compare(game1.popularity, popularity) == 0 &&
        Double.compare(game1.totalRating, totalRating) == 0 &&
        hypes == game1.hypes &&
        collection == game1.collection &&
        ratingCount == game1.ratingCount &&
        totalRatingCount == game1.totalRatingCount &&
        Double.compare(game1.aggregatedRating, aggregatedRating) == 0 &&
        category == game1.category &&
        status == game1.status &&
        Objects.equals(franchise, game1.franchise) &&
        Objects.equals(keywords, game1.keywords) &&
        Objects.equals(developers, game1.developers) &&
        Objects.equals(videos, game1.videos) &&
        Objects.equals(alternativeNames, game1.alternativeNames) &&
        Objects.equals(timeToBeat, game1.timeToBeat) &&
        Objects.equals(playerPerspectives, game1.playerPerspectives) &&
        Objects.equals(screenshots, game1.screenshots) &&
        Objects.equals(cover, game1.cover) &&
        Objects.equals(themes, game1.themes) &&
        Objects.equals(genres, game1.genres) &&
        Objects.equals(storyline, game1.storyline) &&
        Objects.equals(releaseDates, game1.releaseDates) &&
        Objects.equals(games, game1.games) &&
        Objects.equals(publishers, game1.publishers) &&
        Objects.equals(slug, game1.slug) &&
        Objects.equals(summary, game1.summary) &&
        Objects.equals(gameModes, game1.gameModes) &&
        Objects.equals(pegi, game1.pegi) &&
        Objects.equals(url, game1.url) &&
        Objects.equals(tags, game1.tags) &&
        Objects.equals(esrb, game1.esrb) &&
        Objects.equals(name, game1.name) &&
        Objects.equals(gameEngines, game1.gameEngines) &&
        Objects.equals(websites, game1.websites);
  }

  @Override public int hashCode() {

    return Objects.hash(id, franchise, game, keywords, developers, rating, createdAt, videos,
        aggregatedRatingCount, alternativeNames, timeToBeat, playerPerspectives, screenshots, cover,
        themes, updatedAt, genres, firstReleaseDate, storyline, popularity, releaseDates, games,
        publishers, totalRating, slug, hypes, summary, gameModes, pegi, collection, url,
        ratingCount,
        tags, esrb, name, totalRatingCount, aggregatedRating, gameEngines, websites, category,
        status);
  }

  @Override public String toString() {
    return "Game{" +
        "franchise=" + franchise +
        ", game=" + game +
        ", keywords=" + keywords +
        ", developers=" + developers +
        ", rating=" + rating +
        ", createdAt=" + createdAt +
        ", videos=" + videos +
        ", aggregatedRatingCount=" + aggregatedRatingCount +
        ", alternativeNames=" + alternativeNames +
        ", timeToBeat=" + timeToBeat +
        ", playerPerspectives=" + playerPerspectives +
        ", screenshots=" + screenshots +
        ", cover=" + cover +
        ", themes=" + themes +
        ", updatedAt=" + updatedAt +
        ", genres=" + genres +
        ", firstReleaseDate=" + firstReleaseDate +
        ", storyline='" + storyline + '\'' +
        ", popularity=" + popularity +
        ", releaseDates=" + releaseDates +
        ", games=" + games +
        ", publishers=" + publishers +
        ", totalRating=" + totalRating +
        ", id=" + id +
        ", slug='" + slug + '\'' +
        ", hypes=" + hypes +
        ", summary='" + summary + '\'' +
        ", gameModes=" + gameModes +
        ", pegi=" + pegi +
        ", collection=" + collection +
        ", url='" + url + '\'' +
        ", ratingCount=" + ratingCount +
        ", tags=" + tags +
        ", esrb=" + esrb +
        ", name='" + name + '\'' +
        ", totalRatingCount=" + totalRatingCount +
        ", aggregatedRating=" + aggregatedRating +
        ", gameEngines=" + gameEngines +
        ", websites=" + websites +
        ", category=" + category +
        ", status=" + status +
        '}';
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<Long> getFranchise() {
    return franchise;
  }

  public void setFranchise(List<Long> franchise) {
    this.franchise = franchise;
  }

  public long getGame() {
    return game;
  }

  public void setGame(long game) {
    this.game = game;
  }

  public List<Long> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<Long> keywords) {
    this.keywords = keywords;
  }

  public List<Company> getDevelopers() {
    return developers;
  }

  public void setDevelopers(List<Company> developers) {
    this.developers = developers;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public List<Video> getVideos() {
    return videos;
  }

  public void setVideos(List<Video> videos) {
    this.videos = videos;
  }

  public int getAggregatedRatingCount() {
    return aggregatedRatingCount;
  }

  public void setAggregatedRatingCount(int aggregatedRatingCount) {
    this.aggregatedRatingCount = aggregatedRatingCount;
  }

  public List<AlternativeName> getAlternativeNames() {
    return alternativeNames;
  }

  public void setAlternativeNames(
      List<AlternativeName> alternativeNames) {
    this.alternativeNames = alternativeNames;
  }

  public TimeToBeat getTimeToBeat() {
    return timeToBeat;
  }

  public void setTimeToBeat(TimeToBeat timeToBeat) {
    this.timeToBeat = timeToBeat;
  }

  public List<Long> getPlayerPerspectives() {
    return playerPerspectives;
  }

  public void setPlayerPerspectives(List<Long> playerPerspectives) {
    this.playerPerspectives = playerPerspectives;
  }

  public List<Image> getScreenshots() {
    return screenshots;
  }

  public void setScreenshots(List<Image> screenshots) {
    this.screenshots = screenshots;
  }

  public Image getCover() {
    return cover;
  }

  public void setCover(Image cover) {
    this.cover = cover;
  }

  public List<Theme> getThemes() {
    return themes;
  }

  public void setThemes(List<Theme> themes) {
    this.themes = themes;
  }

  public long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<Long> getGenres() {
    return genres;
  }

  public void setGenres(List<Long> genres) {
    this.genres = genres;
  }

  public long getFirstReleaseDate() {
    return firstReleaseDate;
  }

  public void setFirstReleaseDate(long firstReleaseDate) {
    this.firstReleaseDate = firstReleaseDate;
  }

  public String getStoryline() {
    return storyline;
  }

  public void setStoryline(String storyline) {
    this.storyline = storyline;
  }

  public double getPopularity() {
    return popularity;
  }

  public void setPopularity(double popularity) {
    this.popularity = popularity;
  }

  public List<ReleaseDate> getReleaseDates() {
    return releaseDates;
  }

  public void setReleaseDates(
      List<ReleaseDate> releaseDates) {
    this.releaseDates = releaseDates;
  }

  public List<Long> getGames() {
    return games;
  }

  public void setGames(List<Long> games) {
    this.games = games;
  }

  public List<Company> getPublishers() {
    return publishers;
  }

  public void setPublishers(List<Company> publishers) {
    this.publishers = publishers;
  }

  public double getTotalRating() {
    return totalRating;
  }

  public void setTotalRating(double totalRating) {
    this.totalRating = totalRating;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public int getHypes() {
    return hypes;
  }

  public void setHypes(int hypes) {
    this.hypes = hypes;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public List<Long> getGameModes() {
    return gameModes;
  }

  public void setGameModes(List<Long> gameModes) {
    this.gameModes = gameModes;
  }

  public Pegi getPegi() {
    return pegi;
  }

  public void setPegi(Pegi pegi) {
    this.pegi = pegi;
  }

  public long getCollection() {
    return collection;
  }

  public void setCollection(long collection) {
    this.collection = collection;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getRatingCount() {
    return ratingCount;
  }

  public void setRatingCount(int ratingCount) {
    this.ratingCount = ratingCount;
  }

  public List<Integer> getTags() {
    return tags;
  }

  public void setTags(List<Integer> tags) {
    this.tags = tags;
  }

  public Esrb getEsrb() {
    return esrb;
  }

  public void setEsrb(Esrb esrb) {
    this.esrb = esrb;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTotalRatingCount() {
    return totalRatingCount;
  }

  public void setTotalRatingCount(int totalRatingCount) {
    this.totalRatingCount = totalRatingCount;
  }

  public double getAggregatedRating() {
    return aggregatedRating;
  }

  public void setAggregatedRating(double aggregatedRating) {
    this.aggregatedRating = aggregatedRating;
  }

  public List<Long> getGameEngines() {
    return gameEngines;
  }

  public void setGameEngines(List<Long> gameEngines) {
    this.gameEngines = gameEngines;
  }

  public List<Website> getWebsites() {
    return websites;
  }

  public void setWebsites(List<Website> websites) {
    this.websites = websites;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}