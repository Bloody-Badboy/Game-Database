package me.bloodybadboy.gamedatabase.data.source.local.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import java.util.List;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.PulseEntity;

@Dao
public abstract class PulsesDao {

  @Query("SELECT * FROM game_pulses")
  public abstract List<PulseEntity> getAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public abstract void insertAll(List<PulseEntity> pulseEntities);

  @Query("DELETE FROM game_pulses")
  abstract void deleteAll();

  @Query("DELETE FROM game_pulses WHERE pulse_id = :pulseId")
  abstract void delete(long pulseId);

  @Transaction
  public void deletePulses(List<PulseEntity> pulseEntities) {
    for (PulseEntity entity : pulseEntities) {
      delete(entity.getId());
    }
  }

  @Transaction
  public void updatePulses(List<PulseEntity> pulseEntities) {
    deleteAll();
    insertAll(pulseEntities);
  }
}
