package me.bloodybadboy.gamedatabase.data.source;

import java.util.List;
import java.util.Map;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import retrofit2.Call;

public interface GameDataSource {
  Call<List<Game>> listGames(Map<String, String> queryMap);

  Call<List<Game>> getGamesDetails(String gameIds, Map<String, String> queryMap);

  Call<List<Pulse>> getGameNews(Map<String, String> queryMap);

  List<Game> listFavouriteGames();

  List<Long> listFavouriteGameIds();

  void addGameToFavourites(Game game);

  void removeGameFromFavourites(long gameId);

  boolean isGameInFavourites(long gameId);

  List<Pulse> listNewsFromDatabase();

  void addNewsToDatabase(List<Pulse> pulses);

  void removeNewsFromDatabase(List<Pulse> pulses);

  void updateNewsDatabase(List<Pulse> pulses);
}
