package cinemo.metar.room;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import cinemo.metar.utils.executors.DefaultExecutorSupplier;

/**
 * Created by Shahbaz Hashmi on 2020-01-29.
 */
public class StationRepository {

    public static final String TAG = "StationRepository";

    private Application mApplication;

    private StationDao mStationDao;

    private LiveData<List<Station>> mStationList;

    StationRepository(Application application) {
        this.mApplication = application;
        StationDatabase database = StationDatabase.getInstance(
                application.getApplicationContext());

        mStationDao = database.stationDao();
        mStationList = mStationDao.getAllStations();
    }

    public void insert(Station station) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.insertStation(station);
        });
    }

    public void InsertUpdated(Station station) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            if(mStationDao.getStationByData(station.getFileName(), station.getDateModified(), station.getSize()) == null) {
                mStationDao.insertStation(station);
            } else {
                Log.d(TAG, "identical record found -> "+station.getFileName());
            }
        });
    }

    public void update(Station station) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.updateStation(station);
        });
    }

    public LiveData<List<Station>> getAllStations() {
        return mStationDao.getAllStations();
    }

    public void deleteAll() {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.deleteAllStations();
        });
    }

}
