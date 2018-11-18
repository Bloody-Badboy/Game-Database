package me.bloodybadboy.gamedatabase.data.source.remote;

import java.util.List;
import java.util.Map;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBService;
import retrofit2.Call;

public final class RemoteGameDataSource implements GameDataSource {

  private static volatile RemoteGameDataSource sInstance = null;
  private IGDBService igdbService;

  private RemoteGameDataSource(IGDBService igdbService) {
    this.igdbService = igdbService;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + RemoteGameDataSource.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static RemoteGameDataSource getInstance(IGDBService igdbService) {
    if (sInstance == null) {
      synchronized (RemoteGameDataSource.class) {
        if (sInstance == null) {
          sInstance = new RemoteGameDataSource(igdbService);
        }
      }
    }
    return sInstance;
  }

  @Override public Call<List<Game>> listGames(Map<String, String> queryMap) {
    return igdbService.listGames(queryMap);
  }

  @Override public Call<List<Game>> getGamesDetails(String gameIds, Map<String, String> queryMap) {
    return igdbService.gamesDetails(gameIds, queryMap);
  }

  @Override public Call<List<Pulse>> getGameNews(Map<String, String> queryMap) {
    return igdbService.gameNews(queryMap);
  }

  @Override public List<Game> listFavouriteGames() {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public List<Long> listFavouriteGameIds() {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public void addGameToFavourites(Game game) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public void removeGameFromFavourites(long gameId) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public boolean isGameInFavourites(long gameId) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public List<Pulse> listNewsFromDatabase() {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public void addNewsToDatabase(List<Pulse> pulses) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public void removeNewsFromDatabase(List<Pulse> pulses) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public void updateNewsDatabase(List<Pulse> pulses) {
    throw new UnsupportedOperationException("Method not implemented.");
  }
}
