package me.bloodybadboy.gamedatabase.domain.usecase;

import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public final class RemoveGameFromFavouritesUseCase extends LiveDataUseCase<Void> {

  private GameDataRepository repository;
  private long gameId;

  public RemoveGameFromFavouritesUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;
  }

  public RemoveGameFromFavouritesUseCase setGame(long gameId) {
    this.gameId = gameId;
    return this;
  }

  @Override protected Result<Void> execute() {
    repository.removeGameFromFavourites(gameId);
    return Result.Success(null);
  }
}
