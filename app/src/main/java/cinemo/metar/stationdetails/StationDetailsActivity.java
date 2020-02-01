package cinemo.metar.stationdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import cinemo.metar.R;
import cinemo.metar.databinding.ActivityStationDetailsBinding;
import cinemo.metar.interfaces.FetchDetailDataListener;
import cinemo.metar.main.MainActivity;
import cinemo.metar.room.Station;
import cinemo.metar.utils.AppUtils;

public class StationDetailsActivity extends AppCompatActivity {

    private ActivityStationDetailsBinding mBinding;
    private StationDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_station_details);
        mViewModel = ViewModelProviders.of(this).get(StationDetailsViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);
        mViewModel.loaderHelper.setRetryListener(this::loadData);
        loadData();
    }

    private void loadData() {
        mViewModel.loaderHelper.showLoading();
        mViewModel.stationViewModel.fetchAndGetDetailData((Station) getIntent().getParcelableExtra(MainActivity.STATION_OBJECT), new FetchDetailDataListener() {
            @Override
            public void onSuccess(Station station) {
                mViewModel.stationLiveData.setValue(station);
                mViewModel.loaderHelper.dismiss();
            }

            @Override
            public void onUpdatedData(Station station) {
                //todo - refresh
                AppUtils.showToast(StationDetailsActivity.this, getString(R.string.txt_new_data_found), true);
            }

            @Override
            public void onError(String errMsg, boolean canRetry) {
                if(canRetry) {
                    mViewModel.loaderHelper.showErrorWithRetry(errMsg);
                } else {
                    mViewModel.loaderHelper.showError(errMsg);
                }
            }

            @Override
            public void onErrorPrompt(String errMsg) {
                AppUtils.showToast(StationDetailsActivity.this, errMsg, true);
            }
        });
    }
}