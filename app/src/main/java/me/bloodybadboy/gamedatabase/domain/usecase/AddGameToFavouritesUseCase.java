package me.bloodybadboy.gamedatabase.domain.usecase;

import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.domain.usecase.base.LiveDataUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public final class AddGameToFavouritesUseCase extends LiveDataUseCase<Void> {

  private GameDataRepository repository;
  private Game game;

  public AddGameToFavouritesUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;
  }

  public AddGameToFavouritesUseCase setGame(Game game) {
    this.game = game;
    return this;
  }

  @Override protected Result<Void> execute() {
    repository.addGameToFavourites(game);
    return Result.Success(null);
  }
}
