package hu.anita.lifeassistant.cleaningduty.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import hu.anita.lifeassistant.cleaningduty.db.helper.AssetSQLiteOpenHelperFactory;

/**
 * Created by Anita on 2018.08.30..
 */

@Database(entities = {CleaningDuty.class}, version = 3)
public abstract class CleaningDutyDatabase extends RoomDatabase {
    public abstract CleaningDutyDao cleaningDutyDao();
    private static CleaningDutyDatabase INSTANCE;
    private static final String DB_NAME = "lifeassistant.db";

    public static CleaningDutyDatabase getDatabase(final Context context) {
        //context.deleteDatabase(DB_NAME);
        if (INSTANCE == null) {
            synchronized (CleaningDutyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CleaningDutyDatabase.class, DB_NAME)
                            .openHelperFactory(new AssetSQLiteOpenHelperFactory())
                            .allowMainThreadQueries()
                            //TODO remove, create dialog fragment
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
