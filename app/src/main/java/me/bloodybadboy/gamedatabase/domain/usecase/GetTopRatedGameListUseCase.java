package me.bloodybadboy.gamedatabase.domain.usecase;

import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public final class GetTopRatedGameListUseCase extends AbstractGetGameListUseCase {

  @Inject GetTopRatedGameListUseCase(Scheduler scheduler,
      GameDataRepository repository) {
    super(scheduler, repository);
  }

  //
  //name,cover,url,rating,genres.name,release_dates.human,release_dates.date,release_dates.y,first_release_date
  //,summary,aggregated_rating,aggregated_rating_count,rating_count,updated_at

  @Override void addOrderParameter() {
    parameters.addOrder("rating:desc");
  }
}
