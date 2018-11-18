package me.bloodybadboy.gamedatabase.domain.usecase;

import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public final class GetPopularGameListUseCase extends AbstractGetGameListUseCase {

  public GetPopularGameListUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler, repository);
  }

  @Override void addOrderParameter() {
    parameters.addOrder("popularity:desc");
  }
}
