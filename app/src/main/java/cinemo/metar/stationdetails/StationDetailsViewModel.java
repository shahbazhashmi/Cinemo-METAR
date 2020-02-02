package cinemo.metar.stationdetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import cinemo.metar.loader.LoaderHelper;
import cinemo.metar.room.Station;
import cinemo.metar.room.StationViewModel;

/**
 * Created by Shahbaz Hashmi on 2020-02-01.
 */
public class StationDetailsViewModel extends AndroidViewModel {

    public LoaderHelper loaderHelper;
    public StationViewModel stationViewModel;

    public MutableLiveData<Station> stationLiveData = new MutableLiveData<>();
    public LiveData<String> stationData;

    public StationDetailsViewModel(@NonNull Application application) {
        super(application);
        loaderHelper = new LoaderHelper();
        stationViewModel = new StationViewModel(application);
        /**
         * using map to reflect new data when station value gets updated
         */
        stationData = Transformations.map(stationLiveData, Station::getData);
    }
}
