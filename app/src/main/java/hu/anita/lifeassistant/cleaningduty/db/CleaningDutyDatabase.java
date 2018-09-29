package hu.anita.lifeassistant.cleaningduty.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.huma.room_for_asset.RoomAsset;

/**
 * Created by Anita on 2018.08.30..
 */

@Database(entities = {CleaningDuty.class}, version = 2)
public abstract class CleaningDutyDatabase extends RoomDatabase {
    public abstract CleaningDutyDao cleaningDutyDao();
    private static CleaningDutyDatabase INSTANCE;
    private static final String DB_NAME = "lifeassistant.db";

    public static CleaningDutyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CleaningDutyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = RoomAsset.databaseBuilder(context.getApplicationContext(),
                            CleaningDutyDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            //TODO remove, create dialog fragment
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
