package me.bloodybadboy.gamedatabase.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.bloodybadboy.gamedatabase.di.annotations.scope.PerActivity;
import me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity;
import me.bloodybadboy.gamedatabase.ui.games.GameListActivity;
import me.bloodybadboy.gamedatabase.ui.search.GameSearchActivity;

@Module
public abstract class ActivityBindingModule {

  @PerActivity
  @ContributesAndroidInjector
  public abstract GameListActivity contributeGameListActivity();

  @PerActivity
  @ContributesAndroidInjector
  public abstract GameDetailsActivity contributeGameDetailsActivity();

  @PerActivity
  @ContributesAndroidInjector
  public abstract GameSearchActivity contributeGameSearchActivity();
}
