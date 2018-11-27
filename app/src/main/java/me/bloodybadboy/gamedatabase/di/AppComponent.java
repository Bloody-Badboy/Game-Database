package me.bloodybadboy.gamedatabase.di;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;
import me.bloodybadboy.gamedatabase.GameDatabaseApplication;
import me.bloodybadboy.gamedatabase.di.modules.ActivityBindingModule;
import me.bloodybadboy.gamedatabase.di.modules.AppModule;
import me.bloodybadboy.gamedatabase.di.modules.ServiceBindingModule;

@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,
    ActivityBindingModule.class,
    ServiceBindingModule.class
})
public interface AppComponent extends AndroidInjector<GameDatabaseApplication> {
  @Component.Builder abstract class Builder
      extends AndroidInjector.Builder<GameDatabaseApplication> {
  }
}
