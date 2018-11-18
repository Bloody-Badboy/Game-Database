package me.bloodybadboy.gamedatabase.data.source.local.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.GameEntity;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class FavouriteGamesDao {

  @Query("SELECT * FROM favourite_games")
  public abstract List<GameEntity> getAll();

  @Query("SELECT id FROM favourite_games")
  public abstract List<Long> getAllGameIds();

  @Query("SELECT * FROM favourite_games WHERE id = :gameId")
  public abstract GameEntity getGameById(long gameId);

  @Insert(onConflict = REPLACE)
  public abstract void insert(GameEntity entity);

  @Query("DELETE FROM favourite_games WHERE id = :gameId")
  public abstract void deleteGameById(long gameId);
}
