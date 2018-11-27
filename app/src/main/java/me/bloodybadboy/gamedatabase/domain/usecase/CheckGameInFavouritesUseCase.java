package me.bloodybadboy.gamedatabase.domain.usecase;

import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public final class CheckGameInFavouritesUseCase extends LiveDataUseCase<Boolean> {
  private final GameDataSource repository;
  private long gameId;

  @Inject CheckGameInFavouritesUseCase(Scheduler scheduler,
      GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;
  }

  public CheckGameInFavouritesUseCase setGame(long gameId) {
    this.gameId = gameId;
    return this;
  }

  @Override protected Result<Boolean> execute() {
    return Result.Success(repository.isGameInFavourites(gameId));
  }
}
