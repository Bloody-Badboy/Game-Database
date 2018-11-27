package me.bloodybadboy.gamedatabase.di.modules;

import dagger.Module;
import dagger.Provides;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.data.source.GameDataSource;
import me.bloodybadboy.gamedatabase.data.source.local.LocalGameDataSource;
import me.bloodybadboy.gamedatabase.data.source.local.db.GameDatabase;
import me.bloodybadboy.gamedatabase.data.source.remote.RemoteGameDataSource;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBService;
import me.bloodybadboy.gamedatabase.di.annotations.qualifier.LocalDataSource;
import me.bloodybadboy.gamedatabase.di.annotations.qualifier.RemoteDataSource;

@Module
public class RepoModule {

  @Provides
  @LocalDataSource GameDataSource provideLocalDataSource(GameDatabase database) {
    return LocalGameDataSource.getInstance(database);
  }

  @Provides
  @RemoteDataSource GameDataSource provideRemoteDataSource(IGDBService igdbService) {
    return RemoteGameDataSource.getInstance(igdbService);
  }

  @Provides GameDataRepository provideDataRepository(
      @LocalDataSource GameDataSource localGameDataSource,
      @RemoteDataSource GameDataSource remoteGameDataSource) {
    return GameDataRepository.getInstance(localGameDataSource, remoteGameDataSource);
  }
}
