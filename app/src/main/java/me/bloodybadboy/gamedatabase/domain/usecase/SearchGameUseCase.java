package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBParameters;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;
import retrofit2.HttpException;
import retrofit2.Response;

import static me.bloodybadboy.gamedatabase.Constants.GAME_LIST_LIMIT;
import static me.bloodybadboy.gamedatabase.Constants.THEME_EROTIC_ID;

public final class SearchGameUseCase extends LiveDataUseCase<List<Game>> {

  private final GameDataRepository repository;
  private final IGDBParameters parameters = new IGDBParameters();

  public SearchGameUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;

    parameters.addField("name")
        .addField("cover")
        .addLimit(GAME_LIST_LIMIT)
        .addFilter("[cover][exists]", "")
        .addFilter("[themes][not_in]", String.valueOf(THEME_EROTIC_ID));
  }

  public void setSearchQuery(String searchQuery) {
    parameters.addSearch(searchQuery);
  }

  @Override protected Result<List<Game>> execute() throws Exception {
    Response<List<Game>> response = repository.listGames(parameters.getQueryMap()).execute();
    if (response.isSuccessful()) {
      return Result.Success(response.body());
    } else {
      return Result.Error(new HttpException(response));
    }
  }
}
