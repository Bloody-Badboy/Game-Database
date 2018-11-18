package me.bloodybadboy.gamedatabase.data.source;

import java.util.List;
import java.util.Map;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import retrofit2.Call;

public final class GameDataRepository implements GameDataSource {
  private static volatile GameDataRepository sInstance = null;
  private final GameDataSource localGameDataSource;
  private final GameDataSource remoteGameDataSource;

  private GameDataRepository(
      GameDataSource localGameDataSource,
      GameDataSource remoteGameDataSource) {
    this.localGameDataSource = localGameDataSource;
    this.remoteGameDataSource = remoteGameDataSource;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + GameDataRepository.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static GameDataRepository getInstance(
      GameDataSource localGameDataSource,
      GameDataSource remoteGameDataSource) {
    if (sInstance == null) {
      synchronized (GameDataRepository.class) {
        if (sInstance == null) {
          sInstance = new GameDataRepository(localGameDataSource, remoteGameDataSource);
        }
      }
    }
    return sInstance;
  }

  @Override public Call<List<Game>> listGames(Map<String, String> queryMap) {
    return remoteGameDataSource.listGames(queryMap);
  }

  @Override public Call<List<Game>> getGamesDetails(String gameIds, Map<String, String> queryMap) {
    return remoteGameDataSource.getGamesDetails(gameIds, queryMap);
  }

  @Override public Call<List<Pulse>> getGameNews(Map<String, String> queryMap) {
    return remoteGameDataSource.getGameNews(queryMap);
  }

  @Override public List<Game> listFavouriteGames() {
    return localGameDataSource.listFavouriteGames();
  }

  @Override public List<Long> listFavouriteGameIds() {
    return localGameDataSource.listFavouriteGameIds();
  }

  @Override public void addGameToFavourites(Game game) {
    localGameDataSource.addGameToFavourites(game);
  }

  @Override public void removeGameFromFavourites(long gameId) {
    localGameDataSource.removeGameFromFavourites(gameId);
  }

  @Override public boolean isGameInFavourites(long gameId) {
    return localGameDataSource.isGameInFavourites(gameId);
  }

  @Override public List<Pulse> listNewsFromDatabase() {
    return localGameDataSource.listNewsFromDatabase();
  }

  @Override public void addNewsToDatabase(List<Pulse> pulses) {
    localGameDataSource.addNewsToDatabase(pulses);
  }

  @Override public void removeNewsFromDatabase(List<Pulse> pulses) {
    localGameDataSource.removeNewsFromDatabase(pulses);
  }

  @Override public void updateNewsDatabase(List<Pulse> pulses) {
    localGameDataSource.updateNewsDatabase(pulses);
  }
}