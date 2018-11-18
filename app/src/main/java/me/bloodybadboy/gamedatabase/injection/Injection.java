package me.bloodybadboy.gamedatabase.injection;

import android.content.Context;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.moshi.Moshi;
import me.bloodybadboy.gamedatabase.BuildConfig;
import me.bloodybadboy.gamedatabase.Constants;
import me.bloodybadboy.gamedatabase.GameDatabaseApplication;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.local.LocalGameDataSource;
import me.bloodybadboy.gamedatabase.data.source.local.db.GameDatabase;
import me.bloodybadboy.gamedatabase.data.source.remote.RemoteGameDataSource;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBInterceptor;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBService;
import me.bloodybadboy.gamedatabase.utils.scheduler.AsyncScheduler;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

@SuppressWarnings("WeakerAccess") public final class Injection {
  private static volatile OkHttpClient sOkHttpClientInstance = null;
  private static volatile Retrofit sRetrofitInstance = null;
  private static volatile Moshi sMoshiInstance = null;

  private Injection() {
    throw new AssertionError();
  }

  public static Context provideApplicationContext() {
    return GameDatabaseApplication.getInstance().getApplicationContext();
  }

  public static String provideUserKey() {
    return Injection.provideApplicationContext().getResources().getString(R.string.igdb_api_key);
  }

  public static OkHttpClient provideOkHttpClient() {
    if (sOkHttpClientInstance == null) {
      synchronized (Injection.class) {
        if (sOkHttpClientInstance == null) {
          sOkHttpClientInstance = new OkHttpClient.Builder()
              .addNetworkInterceptor(new IGDBInterceptor(Injection.provideUserKey()))
              .addNetworkInterceptor(new StethoInterceptor())
              .addNetworkInterceptor(new HttpLoggingInterceptor(
                  message -> Timber.d(message)).setLevel(BuildConfig.DEBUG ? HEADERS : NONE))
              .build();
        }
      }
    }
    return sOkHttpClientInstance;
  }

  public static Retrofit provideRetrofit() {
    if (sRetrofitInstance == null) {
      synchronized (Injection.class) {
        if (sRetrofitInstance == null) {
          sRetrofitInstance = new Retrofit.Builder()
              .addConverterFactory(MoshiConverterFactory.create())
              .client(provideOkHttpClient())
              .baseUrl(Constants.API_SERVICE_ENDPOINT)
              .build();
        }
      }
    }
    return sRetrofitInstance;
  }

  public static Moshi provideMoshi() {
    if (sMoshiInstance == null) {
      synchronized (Injection.class) {
        if (sMoshiInstance == null) {
          sMoshiInstance = new Moshi.Builder().build();
        }
      }
    }
    return sMoshiInstance;
  }

  private static IGDBService provideIGDBApiService(Retrofit retrofit) {
    return retrofit.create(IGDBService.class);
  }

  private static GameDatabase provideGameDatabase() {
    return GameDatabase.getInstance(Injection.provideApplicationContext());
  }

  private static GameDataSource provideLocalDataSource() {
    return LocalGameDataSource.getInstance(Injection.provideGameDatabase());
  }

  private static GameDataSource provideRemoteDataSource() {
    return RemoteGameDataSource.getInstance(
        Injection.provideIGDBApiService(Injection.provideRetrofit()));
  }

  public static GameDataRepository provideDataRepository() {
    return GameDataRepository.getInstance(Injection.provideLocalDataSource(),
        Injection.provideRemoteDataSource());
  }

  public static Scheduler provideAppScheduler() {
    return AsyncScheduler.getInstance();
  }
}
