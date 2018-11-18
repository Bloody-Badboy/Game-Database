package me.bloodybadboy.gamedatabase.data.source.local;

import java.util.List;
import java.util.Map;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.local.db.GameDatabase;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.GameEntity;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.mapper.GameEntityDataMapper;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.mapper.PulseEntityDataMapper;
import retrofit2.Call;

public final class LocalGameDataSource implements GameDataSource {

  private static volatile LocalGameDataSource sInstance = null;
  private final GameEntityDataMapper gameEntityDataMapper = new GameEntityDataMapper();
  private final PulseEntityDataMapper pulseEntityDataMapper = new PulseEntityDataMapper();
  private GameDatabase gameDatabase;

  private LocalGameDataSource(GameDatabase gameDatabase) {
    this.gameDatabase = gameDatabase;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + LocalGameDataSource.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static LocalGameDataSource getInstance(GameDatabase gameDatabase) {
    if (sInstance == null) {
      synchronized (LocalGameDataSource.class) {
        if (sInstance == null) {
          sInstance = new LocalGameDataSource(gameDatabase);
        }
      }
    }
    return sInstance;
  }

  @Override public Call<List<Game>> listGames(Map<String, String> queryMap) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public Call<List<Game>> getGamesDetails(String gameIds, Map<String, String> queryMap) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public Call<List<Pulse>> getGameNews(Map<String, String> queryMap) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public List<Game> listFavouriteGames() {
    return gameEntityDataMapper.transformToGame(gameDatabase.favouriteGamesDao().getAll());
  }

  @Override public List<Long> listFavouriteGameIds() {
    return gameDatabase.favouriteGamesDao().getAllGameIds();
  }

  @Override public void addGameToFavourites(Game game) {
    gameDatabase.favouriteGamesDao().insert(gameEntityDataMapper.transform(game));
  }

  @Override public void removeGameFromFavourites(long gameId) {
    gameDatabase.favouriteGamesDao().deleteGameById(gameId);
  }

  @Override public boolean isGameInFavourites(long gameId) {
    GameEntity gameEntity = gameDatabase.favouriteGamesDao().getGameById(gameId);
    return gameEntity != null;
  }

  @Override public List<Pulse> listNewsFromDatabase() {
    return pulseEntityDataMapper.transformToPulse(gameDatabase.pulsesDao().getAll());
  }

  @Override public void addNewsToDatabase(List<Pulse> pulses) {
    gameDatabase.pulsesDao().insertAll(pulseEntityDataMapper.transformToEntity(pulses));
  }

  @Override public void removeNewsFromDatabase(List<Pulse> pulses) {
    gameDatabase.pulsesDao().deletePulses(pulseEntityDataMapper.transformToEntity(pulses));
  }

  @Override public void updateNewsDatabase(List<Pulse> pulses) {
    gameDatabase.pulsesDao().updatePulses(pulseEntityDataMapper.transformToEntity(pulses));
  }
}
