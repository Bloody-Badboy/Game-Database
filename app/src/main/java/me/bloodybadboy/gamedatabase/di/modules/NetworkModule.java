package me.bloodybadboy.gamedatabase.di.modules;

import android.content.Context;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.bloodybadboy.gamedatabase.BuildConfig;
import me.bloodybadboy.gamedatabase.Constants;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBInterceptor;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBService;
import me.bloodybadboy.gamedatabase.di.annotations.qualifier.UserKey;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

@Module
public class NetworkModule {

  @UserKey
  @Provides
  String provideUserKey(Context context) {
    return context.getResources().getString(R.string.igdb_api_key);
  }

  @Singleton
  @Provides
  OkHttpClient provideOkHttpClient(@UserKey String userKey) {
    return new OkHttpClient.Builder()
        .addNetworkInterceptor(new IGDBInterceptor(userKey))
        .addNetworkInterceptor(new StethoInterceptor())
        .addNetworkInterceptor(new HttpLoggingInterceptor(
            message -> Timber.d(message)).setLevel(BuildConfig.DEBUG ? HEADERS : NONE))
        .build();
  }

  @Singleton
  @Provides
  Retrofit provideRetrofit(OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(Constants.API_SERVICE_ENDPOINT)
        .build();
  }

  @Singleton
  @Provides
  IGDBService provideIGDBApiService(Retrofit retrofit) {
    return retrofit.create(IGDBService.class);
  }
}
