package me.bloodybadboy.gamedatabase.data.source.remote.api;

import java.util.List;
import java.util.Map;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IGDBService {

  @GET("games/")
  Call<List<Game>> listGames(@QueryMap Map<String, String> map);

  @GET("games/{gameIds}")
  Call<List<Game>> gamesDetails(@Path("gameIds") String gameIds, @QueryMap Map<String, String> map);

  @GET("pulses/")
  Call<List<Pulse>> gameNews(@QueryMap Map<String, String> map);
}
