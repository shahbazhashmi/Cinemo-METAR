package cinemo.metar.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import cinemo.metar.loader.LoaderHelper;
import cinemo.metar.room.StationViewModel;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    public LoaderHelper loaderHelper;
    public StationViewModel stationViewModel;
    public StationAdapter stationAdapter;

    public MainViewModel(Application application) {
        super(application);
        loaderHelper = new LoaderHelper();
        stationViewModel = new StationViewModel(application);
        stationAdapter = new StationAdapter();
    }

}
