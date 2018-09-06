package hu.anita.lifeassistant.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Anita on 2018.08.30..
 */
@Dao
public interface CleaningDutyDao {
    @Query("SELECT * FROM cleaning_duties")
    LiveData<List<CleaningDuty>> getAll();

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(CleaningDuty director);
}
