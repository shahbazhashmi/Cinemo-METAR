package cinemo.metar.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by Shahbaz Hashmi on 2020-01-28.
 */
@Dao
public interface StationDao {

    @Query("SELECT * FROM station")
    LiveData<List<Station>> getAllStations();

    @Query("DELETE FROM station")
    void deleteAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStation(Station station);

    @Update
    void updateStation(Station station);

}
