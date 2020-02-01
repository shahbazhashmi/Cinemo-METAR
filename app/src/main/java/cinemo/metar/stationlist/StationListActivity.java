package cinemo.metar.stationlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import cinemo.metar.R;
import cinemo.metar.databinding.ActivityStationListBinding;
import cinemo.metar.interfaces.FetchListDataListener;
import cinemo.metar.room.Station;
import cinemo.metar.stationdetails.StationDetailsActivity;
import cinemo.metar.utils.AppUtils;

public class StationListActivity extends AppCompatActivity {

    public static final String STATION_OBJECT = "station_object";

    private ActivityStationListBinding mBinding;
    private StationListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_station_list);
        mViewModel = ViewModelProviders.of(this).get(StationListViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.stationRv.setAdapter(mViewModel.stationAdapter);
        mViewModel.loaderHelper.setRetryListener(this::loadData);
        mViewModel.stationAdapter.setOnStationClickListener(this::navigateToStationActivity);
        loadData();
    }


    private void loadData() {
        mViewModel.loaderHelper.showLoading();
        mViewModel.stationViewModel.fetchAndGetListData(new FetchListDataListener() {
            @Override
            public void onSuccess(LiveData<List<Station>> stationList) {
                stationList.observe(StationListActivity.this, data -> {
                    mViewModel.stationAdapter.setStations(data);
                });
                mViewModel.stationAdapter.notifyDataSetChanged();
                mViewModel.loaderHelper.dismiss();
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
            public void onUpdatedData(LiveData<List<Station>> stationList) {
                AppUtils.setSnackBar(mBinding.parentLt, getString(R.string.txt_new_data_found), view -> {
                    mViewModel.loaderHelper.showLoading();
                    stationList.observe(StationListActivity.this, data -> {
                        mViewModel.stationAdapter.setStations(data);
                    });
                    mViewModel.stationAdapter.notifyDataSetChanged();
                    mViewModel.loaderHelper.dismiss();
                });
            }

            @Override
            public void onErrorPrompt(String errMsg) {
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
                if(!mViewModel.loaderHelper.isLoading) {
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
