package me.bloodybadboy.gamedatabase;

import androidx.annotation.NonNull;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import me.bloodybadboy.gamedatabase.di.DaggerAppComponent;
import timber.log.Timber;

public class GameDatabaseApplication extends DaggerApplication {

  private static GameDatabaseApplication sInstance;

  public static GameDatabaseApplication getInstance() {
    return sInstance;
  }

  @Override public void onCreate() {
    super.onCreate();

    sInstance = this;

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }

    LeakCanary.install(this);

    if (BuildConfig.DEBUG) {
      plantTimberDebug();
      Stetho.initializeWithDefaults(this);
    }
  }

  private void plantTimberDebug() {
    Timber.plant(new Timber.DebugTree() {
      @Override protected String createStackElementTag(@NonNull StackTraceElement element) {
        return "GameDB - "
            + super.createStackElementTag(element)
            + ":"
            + element.getLineNumber();
      }
    });
  }

  @Override protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return DaggerAppComponent.builder().create(this);
  }
}
