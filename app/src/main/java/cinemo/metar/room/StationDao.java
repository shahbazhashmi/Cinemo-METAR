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
    List<Station> getAllStations();

    @Query("DELETE FROM station")
    void deleteAllStations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStation(Station station);

    @Update
    void updateStation(Station station);

    @Query("SELECT * FROM station WHERE file_name = :fileName")
    Station getStationByFileName(String fileName);

    /**
     * used to identify existing station
     * @param fileName
     * @param dateTime
     * @param size
     * @return
     */
    @Query("SELECT * FROM station WHERE file_name = :fileName AND date_modified = :dateTime AND size = :size")
    Station getStationByData(String fileName, String dateTime, String size);

    @Query("SELECT COUNT(file_name) FROM station")
    int getCount();

}
