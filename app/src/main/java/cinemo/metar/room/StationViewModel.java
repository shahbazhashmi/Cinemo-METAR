package cinemo.metar.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Created by Shahbaz Hashmi on 2020-01-29.
 */
public class StationViewModel extends AndroidViewModel {

    private StationRepository mStationRepository;

    public StationViewModel(@NonNull Application application) {
        super(application);
        mStationRepository = new StationRepository(application);
    }

    public LiveData<List<Station>> getAllStationsList() {
        return mStationRepository.getAllStations();
    }

    public void insertStation(Station station) {
        mStationRepository.insert(station);
    }

    public void updateStation(Station station) {
        mStationRepository.update(station);
    }

    public void deleteAllStations() {
        mStationRepository.deleteAll();
    }


}
