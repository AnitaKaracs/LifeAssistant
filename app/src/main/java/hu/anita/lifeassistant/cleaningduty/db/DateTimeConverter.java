package hu.anita.lifeassistant.cleaningduty.db;

import android.arch.persistence.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by Anita on 2018.08.30..
 */

public class DateTimeConverter {
    @TypeConverter
    public static LocalDateTime fromTimestamp(long value) {
        if (value == 0L) {
            return null;
        }
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value),
                ZoneOffset.UTC.normalized());
    }

    @TypeConverter
    public static long toTimestamp(LocalDateTime date) {
        if (date == null) {
            return 0L;
        }
        return date.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
