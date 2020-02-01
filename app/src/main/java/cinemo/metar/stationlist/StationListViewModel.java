package cinemo.metar.stationlist;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import cinemo.metar.AppController;
import cinemo.metar.R;
import cinemo.metar.loader.LoaderHelper;
import cinemo.metar.room.StationViewModel;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class StationListViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    public LoaderHelper loaderHelper;
    public StationViewModel stationViewModel;
    public StationAdapter stationAdapter;

    public StationListViewModel(Application application) {
        super(application);
        loaderHelper = new LoaderHelper();
        stationViewModel = new StationViewModel(application);
        stationAdapter = new StationAdapter(() -> {
            loaderHelper.showEmptySearch();
            return null;
        });
    }

}
