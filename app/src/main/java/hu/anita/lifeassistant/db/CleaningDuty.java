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
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleaningDuty that = (CleaningDuty) o;
        return id == that.id &&
                checked == that.checked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, checked);
    }
}
