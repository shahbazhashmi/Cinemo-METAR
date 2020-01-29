package cinemo.metar.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Created by Shahbaz Hashmi on 2020-01-29.
 */
public class StationRepository {

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
        new InsertStationAsyncTask().execute(station);
    }

    public void update(Station station) {
        new UpdateStationAsyncTask().execute(station);
    }

    public LiveData<List<Station>> getAllStations() {
        return mStationDao.getAllStations();
    }

    public void deleteAll() {
        new DeleteAllStationSAsyncTask().execute();
    }


    private class InsertStationAsyncTask extends AsyncTask<Station, Void, Void> {
        @Override
        protected Void doInBackground(Station... stations) {
            mStationDao.insertStation(stations[0]);
            return null;
        }
    }

    private class UpdateStationAsyncTask extends AsyncTask<Station, Void, Void> {
        @Override
        protected Void doInBackground(Station... stations) {
            mStationDao.updateStation(stations[0]);
            return null;
        }
    }

    private class DeleteAllStationSAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mStationDao.deleteAllNotes();
            return null;
        }
    }

}
