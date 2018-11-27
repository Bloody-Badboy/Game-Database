package me.bloodybadboy.gamedatabase.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import me.bloodybadboy.gamedatabase.di.annotations.ViewModelKey;
import me.bloodybadboy.gamedatabase.ui.ViewModelFactory;
import me.bloodybadboy.gamedatabase.ui.details.GameDetailsViewModel;
import me.bloodybadboy.gamedatabase.ui.games.GameListViewModel;
import me.bloodybadboy.gamedatabase.ui.search.GameSearchViewModel;

@Module
public abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(GameListViewModel.class)
  public abstract ViewModel bindGameListViewModel(GameListViewModel viewModel);

  @Binds
  @IntoMap
  @ViewModelKey(GameDetailsViewModel.class)
  public abstract ViewModel bindGameDetailsViewModel(GameDetailsViewModel viewModel);

  @Binds
  @IntoMap
  @ViewModelKey(GameSearchViewModel.class)
  public abstract ViewModel bindGameSearchViewModel(GameSearchViewModel viewModel);

  @Binds
  public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
