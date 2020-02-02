package cinemo.metar.stationlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import cinemo.metar.BaseActivity;
import cinemo.metar.R;
import cinemo.metar.databinding.ActivityStationListBinding;
import cinemo.metar.interfaces.FetchListDataListener;
import cinemo.metar.room.Station;
import cinemo.metar.stationdetails.StationDetailsActivity;
import cinemo.metar.utils.AppUtils;

public class StationListActivity extends BaseActivity {

    public static final String STATION_OBJECT = "station_object";

    private ActivityStationListBinding mBinding;
    private StationListViewModel mViewModel;

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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_station_list);
        mViewModel = ViewModelProviders.of(this).get(StationListViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.stationRv.setAdapter(mViewModel.stationAdapter);
        mViewModel.loaderHelper.setRetryListener(this::loadData);
        mViewModel.stationAdapter.setOnStationClickListener(this::navigateToStationActivity);
        setTitle(getString(R.string.txt_station_list));
        loadData();
    }


    private void loadData() {
        mViewModel.loaderHelper.showLoading();
        mViewModel.stationViewModel.fetchAndGetListData(new FetchListDataListener() {
            @Override
            public void onSuccess(List<Station> stationList) {
                mIsErrorOccurred = false;
                mViewModel.stationAdapter.setStations(stationList);
                mViewModel.stationAdapter.notifyDataSetChanged();
                mViewModel.loaderHelper.dismiss();
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
            public void onUpdatedData(List<Station> stationList) {
                mIsErrorOccurred = false;
                AppUtils.setSnackBar(mBinding.parentLt, getString(R.string.txt_new_data_found), view -> {
                    mViewModel.stationAdapter.setStations(stationList);
                    mViewModel.stationAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onErrorPrompt(String errMsg) {
                mIsErrorOccurred = true;
                AppUtils.showToast(StationListActivity.this, errMsg, true);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!mViewModel.loaderHelper.isLoading()) {
                    mViewModel.loaderHelper.dismiss();
                    mViewModel.stationAdapter.getFilter().filter(newText);
                }
                return false;
            }


        });
        return super.onCreateOptionsMenu(menu);
    }

    private void navigateToStationActivity(Station station) {
        Intent intent = new Intent(this, StationDetailsActivity.class);
        intent.putExtra(STATION_OBJECT, station);
        startActivity(intent);
    }


}
