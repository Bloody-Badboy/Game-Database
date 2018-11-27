package me.bloodybadboy.gamedatabase.di.modules;

import android.content.Context;
import com.squareup.moshi.Moshi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.bloodybadboy.gamedatabase.GameDatabaseApplication;
import me.bloodybadboy.gamedatabase.data.source.local.db.GameDatabase;
import me.bloodybadboy.gamedatabase.utils.scheduler.AsyncScheduler;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

@Module(includes = {
    ViewModelModule.class,
    NetworkModule.class,
    RepoModule.class
})
public class AppModule {

  @Provides Context provideApplicationContext() {
    return GameDatabaseApplication.getInstance().getApplicationContext();
  }

  @Singleton
  @Provides Moshi provideMoshi() {
    return new Moshi.Builder().build();
  }

  @Singleton
  @Provides GameDatabase provideGameDatabase(Context context) {
    return GameDatabase.getInstance(context);
  }

  @Singleton
  @Provides Scheduler provideAppScheduler() {
    return new AsyncScheduler();
  }
}
