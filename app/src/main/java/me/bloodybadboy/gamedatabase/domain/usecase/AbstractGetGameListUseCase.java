package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBParameters;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;
import retrofit2.HttpException;
import retrofit2.Response;

import static me.bloodybadboy.gamedatabase.Constants.GAME_LIST_LIMIT;
import static me.bloodybadboy.gamedatabase.Constants.THEME_EROTIC_ID;

public abstract class AbstractGetGameListUseCase extends LiveDataUseCase<List<Game>> {

  final IGDBParameters parameters = new IGDBParameters();
  private final GameDataSource repository;

  AbstractGetGameListUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;

    parameters.addField("name")
        .addField("cover.cloudinary_id")
        .addField("rating")
        .addField("first_release_date")
        .addField("esrb")
        .addField("themes.name")
        .addExpand("themes")
        .addLimit(GAME_LIST_LIMIT)
        .addFilter("[cover][exists]", "")
        .addFilter("[summary][exists]", "")
        .addFilter("[themes][not_in]", String.valueOf(THEME_EROTIC_ID));
    addOrderParameter();
  }

  public void setPaginationOffset(int offset) {
    parameters.addOffset(String.valueOf(offset));
  }

  @Override protected Result<List<Game>> execute() throws Exception {
    Response<List<Game>> response = repository.listGames(parameters.getQueryMap()).execute();
    if (response.isSuccessful()) {
      return Result.Success(response.body());
    } else {
      return Result.Error(new HttpException(response));
    }
  }

  abstract void addOrderParameter();
}
