package me.bloodybadboy.gamedatabase.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.bloodybadboy.gamedatabase.sync.JobSchedulerService;
import me.bloodybadboy.gamedatabase.widget.GameNewsWidgetService;

@Module
public abstract class ServiceBindingModule {

  @ContributesAndroidInjector
  public abstract JobSchedulerService contributeJobSchedulerService();

  @ContributesAndroidInjector
  public abstract GameNewsWidgetService contributeGameNewsWidgetService();
}
