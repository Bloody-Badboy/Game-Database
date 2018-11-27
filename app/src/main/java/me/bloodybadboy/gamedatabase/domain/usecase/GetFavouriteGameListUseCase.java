package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.List;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public final class GetFavouriteGameListUseCase extends LiveDataUseCase<List<Game>> {

  private final GameDataRepository repository;

  @Inject GetFavouriteGameListUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;
  }

  @Override protected Result<List<Game>> execute() {
    return Result.Success(repository.listFavouriteGames());
  }
}
