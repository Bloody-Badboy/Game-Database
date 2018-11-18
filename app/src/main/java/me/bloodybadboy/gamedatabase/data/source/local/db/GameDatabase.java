package me.bloodybadboy.gamedatabase.data.source.local.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.GameEntity;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.PulseEntity;

@Database(entities =
    {
        GameEntity.class,
        PulseEntity.class
    },
    version = 1,
    exportSchema = false)
public abstract class GameDatabase extends RoomDatabase {

  private static volatile GameDatabase sInstance = null;

  public static GameDatabase getInstance(Context context) {
    if (sInstance == null) {
      synchronized (GameDatabase.class) {
        if (sInstance == null) {
          sInstance = Room.databaseBuilder(context.getApplicationContext(), GameDatabase.class,
              "game-database")
              // allow queries on the main thread.
              // Don't do this on a real app! See PersistenceBasicSample for an example.
              .allowMainThreadQueries()
              .build();
        }
      }
    }
    return sInstance;
  }

  public abstract FavouriteGamesDao favouriteGamesDao();

  public abstract PulsesDao pulsesDao();
}
