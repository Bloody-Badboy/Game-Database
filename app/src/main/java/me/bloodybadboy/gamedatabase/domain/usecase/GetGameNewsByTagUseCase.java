package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBParameters;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;
import retrofit2.HttpException;
import retrofit2.Response;

import static me.bloodybadboy.gamedatabase.Constants.PULSE_LIST_LIMIT;

public final class GetGameNewsByTagUseCase extends LiveDataUseCase<List<Pulse>> {

  private final GameDataSource repository;
  private final IGDBParameters parameters = new IGDBParameters();

  public GetGameNewsByTagUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;

    parameters.addField("id")
        .addField("title")
        .addField("published_at")
        .addField("pulse_image.cloudinary_id")
        .addField("url")
        .addField("author")
        .addOrder("published_at:desc")
        .addLimit(PULSE_LIST_LIMIT);
  }

  public GetGameNewsByTagUseCase setGameTagNumber(int tagNumber) {
    parameters.addFilter("[tags][eq]", String.valueOf(tagNumber));
    return this;
  }

  @Override protected Result<List<Pulse>> execute() throws Exception {
    Response<List<Pulse>> response = repository.getGameNews(parameters.getQueryMap()).execute();
    if (response.isSuccessful()) {
      return Result.Success(response.body());
    } else {
      return Result.Error(new HttpException(response));
    }
  }
}
