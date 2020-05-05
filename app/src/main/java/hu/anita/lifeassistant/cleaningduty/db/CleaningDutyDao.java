package hu.anita.lifeassistant.cleaningduty.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
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

    @Query("SELECT * FROM cleaning_duties WHERE checked = 1")
    List<CleaningDuty> getDoneChores();

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(CleaningDuty cleaningDuty);

    @Insert
    void insert(CleaningDuty cleaningDuty);

    @Delete
    void delete(CleaningDuty cleaningDuty);

}
