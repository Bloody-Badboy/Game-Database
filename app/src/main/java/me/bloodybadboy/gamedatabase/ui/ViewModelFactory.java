package me.bloodybadboy.gamedatabase.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.domain.usecase.AddGameToFavouritesUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.AddNewsToDatabaseUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.CheckGameInFavouritesUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetComingSoonGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetFavouriteGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetGameDetailsUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetGameNewsByTagUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetPopularGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetTopRatedGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.RemoveGameFromFavouritesUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.RemoveNewsFromDatabaseUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.SearchGameUseCase;
import me.bloodybadboy.gamedatabase.ui.details.GameDetailsViewModel;
import me.bloodybadboy.gamedatabase.ui.games.GameListViewModel;
import me.bloodybadboy.gamedatabase.ui.search.GameSearchViewModel;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

  private static volatile ViewModelFactory INSTANCE;

  private final Scheduler scheduler;
  private final GameDataRepository repository;

  private ViewModelFactory(Scheduler scheduler, GameDataRepository repository) {
    this.scheduler = scheduler;
    this.repository = repository;
  }

  public static ViewModelFactory getInstance(Scheduler scheduler, GameDataRepository repository) {

    if (INSTANCE == null) {
      synchronized (ViewModelFactory.class) {
        if (INSTANCE == null) {
          INSTANCE = new ViewModelFactory(scheduler, repository);
        }
      }
    }
    return INSTANCE;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(GameListViewModel.class)) {
      //noinspection unchecked
      return (T) new GameListViewModel(
          new GetPopularGameListUseCase(scheduler, repository),
          new GetTopRatedGameListUseCase(scheduler, repository),
          new GetComingSoonGameListUseCase(scheduler, repository),
          new GetFavouriteGameListUseCase(scheduler, repository)
      );
    } else if (modelClass.isAssignableFrom(GameDetailsViewModel.class)) {
      //noinspection unchecked
      return (T) new GameDetailsViewModel(
          new GetGameDetailsUseCase(scheduler, repository),
          new GetGameNewsByTagUseCase(scheduler, repository),
          new AddGameToFavouritesUseCase(scheduler, repository),
          new RemoveGameFromFavouritesUseCase(scheduler, repository),
          new CheckGameInFavouritesUseCase(scheduler, repository),
          new AddNewsToDatabaseUseCase(scheduler, repository),
          new RemoveNewsFromDatabaseUseCase(scheduler, repository)
      );
    } else if (modelClass.isAssignableFrom(GameSearchViewModel.class)) {
      //noinspection unchecked
      return (T) new GameSearchViewModel(
          new SearchGameUseCase(scheduler, repository)
      );
    }
    throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
  }
}
