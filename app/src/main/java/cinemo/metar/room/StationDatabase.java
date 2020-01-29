package cinemo.metar.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by Shahbaz Hashmi on 2020-01-29.
 */
@Database(entities = Station.class, exportSchema = false, version = 1)
public abstract class StationDatabase extends RoomDatabase {
    private static final String DB_NAME = "station_db";
    private static StationDatabase sInstance;

    public static synchronized StationDatabase getInstance(Context context) {
        if(sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), StationDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }

        return sInstance;
    }

    public abstract StationDao stationDao();
}
