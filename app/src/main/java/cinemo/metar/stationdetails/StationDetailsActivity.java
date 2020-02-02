package cinemo.metar.stationdetails;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import cinemo.metar.BaseActivity;
import cinemo.metar.R;
import cinemo.metar.databinding.ActivityStationDetailsBinding;
import cinemo.metar.interfaces.FetchDetailDataListener;
import cinemo.metar.room.Station;
import cinemo.metar.stationlist.StationListActivity;
import cinemo.metar.utils.AppUtils;

public class StationDetailsActivity extends BaseActivity {

    private ActivityStationDetailsBinding mBinding;
    private StationDetailsViewModel mViewModel;

    /**
     * flag to fetch new data when error occurred
     * whether it is showing local data
     */
    private boolean mIsErrorOccurred = false;

    @Override
    protected void onNetworkChange(boolean isConnected) {
        if((!mViewModel.loaderHelper.isShowingData() || mIsErrorOccurred) && isConnected) {
                AppUtils.setSnackBar(mBinding.parentLt, getString(R.string.txt_internet_connected));
                loadData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_station_details);
        mViewModel = ViewModelProviders.of(this).get(StationDetailsViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);
        mViewModel.loaderHelper.setRetryListener(this::loadData);
        setTitle(getString(R.string.txt_station_data));
        loadData();
    }

    private void loadData() {
        mViewModel.loaderHelper.showLoading();
        mViewModel.stationViewModel.fetchAndGetDetailData(getIntent().getParcelableExtra(StationListActivity.STATION_OBJECT), new FetchDetailDataListener() {
            @Override
            public void onSuccess(Station station) {
                mIsErrorOccurred = false;
                mViewModel.stationLiveData.setValue(station);
                mViewModel.loaderHelper.dismiss();
            }

            @Override
            public void onUpdatedData(Station station) {
                mIsErrorOccurred = false;
                AppUtils.setSnackBar(mBinding.parentLt, getString(R.string.txt_new_data_found), view -> {
                    mViewModel.stationLiveData.setValue(station);
                });
            }

            @Override
            public void onError(String errMsg, boolean canRetry) {
                mIsErrorOccurred = true;
                if(canRetry) {
                    mViewModel.loaderHelper.showErrorWithRetry(errMsg);
                } else {
                    mViewModel.loaderHelper.showError(errMsg);
                }
            }

            @Override
            public void onErrorPrompt(String errMsg) {
                mIsErrorOccurred = true;
                AppUtils.showToast(StationDetailsActivity.this, errMsg, true);
            }
        });
    }
}
