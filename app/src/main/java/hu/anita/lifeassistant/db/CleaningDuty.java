package hu.anita.lifeassistant.db;

/**
 * Created by Anita on 2018.08.28..
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.time.LocalDateTime;

@Entity(tableName = "cleaning_duties")
public class CleaningDuty {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "checked")
    @NonNull
    public boolean checked;

    @ColumnInfo(name = "time_required_min")
    @Nullable
    public Integer timeRequiredMin;

    @ColumnInfo(name = "checked_time")
    @TypeConverters({DateTimeConverter.class})
    @Nullable
    public LocalDateTime checkedTime;
}
