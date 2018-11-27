package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.List;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBParameters;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;
import retrofit2.HttpException;
import retrofit2.Response;

public final class GetGameDetailsUseCase extends LiveDataUseCase<Game> {

  private final GameDataSource repository;
  private final IGDBParameters parameters = new IGDBParameters();
  private long gameId;

  @Inject GetGameDetailsUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;

    parameters.addField("name")
        .addField("cover")
        .addField("summary")
        .addField("rating")
        .addField("rating_count")
        .addField("first_release_date")
        .addField("esrb")
        .addField("release_dates")
        .addField("screenshots")
        .addField("videos")
        .addField("url")
        .addField("developers.name")
        .addField("publishers.name")
        .addField("themes.name")
        .addExpand("developers")
        .addExpand("publishers")
        .addExpand("themes");
  }

  public GetGameDetailsUseCase setGameId(long gameId) {
    this.gameId = gameId;
    return this;
  }

  @Override protected Result<Game> execute() throws Exception {
    Response<List<Game>> response =
        repository.getGamesDetails(String.valueOf(gameId), parameters.getQueryMap()).execute();
    if (response.isSuccessful()) {
      if (response.body() != null) {
        return Result.Success(response.body().get(0));
      } else {
        return Result.Success(null);
      }
    } else {
      return Result.Error(new HttpException(response));
    }
  }
}
