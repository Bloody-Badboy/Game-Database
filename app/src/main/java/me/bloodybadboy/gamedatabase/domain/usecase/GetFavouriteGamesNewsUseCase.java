package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBParameters;
import me.bloodybadboy.gamedatabase.domain.usecase.base.UseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;
import retrofit2.HttpException;
import retrofit2.Response;

import static me.bloodybadboy.gamedatabase.Constants.OFFLINE_PULSE_LIST_LIMIT;

public class GetFavouriteGamesNewsUseCase extends UseCase<List<Pulse>> {
  private final GameDataSource repository;
  private final IGDBParameters parameters = new IGDBParameters();
  private final List<Pulse> pulseList = new ArrayList<>();
  private boolean fromDatabase;

  public GetFavouriteGamesNewsUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;

    parameters.addField("id")
        .addField("title")
        .addField("published_at")
        .addField("pulse_image.cloudinary_id")
        .addField("url")
        .addField("author")
        .addOrder("published_at:desc");
  }

  public void setFromDatabase(boolean fromDatabase) {
    this.fromDatabase = fromDatabase;
  }

  @Override protected Result<List<Pulse>> execute() throws Exception {
    if (fromDatabase) {
      return Result.Success(repository.listNewsFromDatabase());
    }
    List<Long> gameIds = repository.listFavouriteGameIds();
    List<Integer> tagNumbers = new ArrayList<>();

    for (long gameId : gameIds) {

      // Tag numbers reference: https://igdb.github.io/api/references/tag-numbers/
      int gameTypeID = 3;
      int gameTagNumber = (gameTypeID << 28) | (int) gameId;

      tagNumbers.add(gameTagNumber);
    }

    for (int tagNumber : tagNumbers) {

      parameters.addFilter("[tags][eq]", String.valueOf(tagNumber));
      parameters.addLimit(OFFLINE_PULSE_LIST_LIMIT);

      Response<List<Pulse>> response = repository.getGameNews(parameters.getQueryMap()).execute();
      if (response.isSuccessful()) {
        List<Pulse> gamePulses = response.body();
        if (gamePulses != null) {
          pulseList.addAll(gamePulses);
        }
      } else {
        return Result.Error(new HttpException(response));
      }
    }

    return Result.Success(pulseList);
  }
}
