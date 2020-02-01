package cinemo.metar.stationdetails;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import cinemo.metar.R;
import cinemo.metar.databinding.ActivityStationDetailsBinding;
import cinemo.metar.interfaces.FetchDetailDataListener;
import cinemo.metar.room.Station;
import cinemo.metar.stationlist.StationListActivity;
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
        mViewModel.stationViewModel.fetchAndGetDetailData(getIntent().getParcelableExtra(StationListActivity.STATION_OBJECT), new FetchDetailDataListener() {
            @Override
            public void onSuccess(Station station) {
                mViewModel.stationLiveData.setValue(station);
                mViewModel.loaderHelper.dismiss();
            }

            @Override
            public void onUpdatedData(Station station) {
                AppUtils.setSnackBar(mBinding.parentLt, getString(R.string.txt_new_data_found), view -> {
                    mViewModel.loaderHelper.showLoading();
                    mViewModel.stationLiveData.setValue(station);
                    mViewModel.loaderHelper.dismiss();
                });
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
