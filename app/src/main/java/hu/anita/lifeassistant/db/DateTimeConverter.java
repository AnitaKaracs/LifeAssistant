package hu.anita.lifeassistant.db;

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
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value),
                ZoneOffset.UTC.normalized());
    }

    @TypeConverter
    public static long toTimestamp(LocalDateTime date) {
        return date.toEpochSecond(ZoneOffset.UTC);
    }
}
