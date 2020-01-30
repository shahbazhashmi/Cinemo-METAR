package cinemo.metar;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import cinemo.metar.interfaces.FetchListDataListener;
import cinemo.metar.loader.LoaderHelper;
import cinemo.metar.room.Station;
import cinemo.metar.room.StationViewModel;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    public LoaderHelper loaderHelper;

    private StationViewModel mStationViewModel;

    public MainViewModel(Application application) {
        super(application);
        loaderHelper = new LoaderHelper(this::loadData);
        mStationViewModel = new StationViewModel(application);

        loadData();
    }

    private void loadData() {

        loaderHelper.showLoading();

        mStationViewModel.fetchListData(new FetchListDataListener() {
            @Override
            public void onSuccess(List<Station> stationList) {
                loaderHelper.dismiss();
            }

            @Override
            public void onError(String errMsg, boolean canRetry) {
                if(canRetry) {
                    loaderHelper.showErrorWithRetry(errMsg);
                } else {
                    loaderHelper.showError(errMsg);
                }
            }
        });

    }
}
