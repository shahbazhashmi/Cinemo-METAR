package cinemo.metar;

import android.os.Handler;

import androidx.lifecycle.ViewModel;

import cinemo.metar.interfaces.LoaderListener;
import cinemo.metar.loader.LoaderHelper;
import cinemo.metar.utils.AppUtils;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class MainViewModel extends ViewModel {

    public LoaderHelper loaderHelper;

    public MainViewModel() {
        loaderHelper = new LoaderHelper(new LoaderListener() {
            @Override
            public void onRetryClick() {
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        if(!AppUtils.isNetworkAvailable(AppController.getInstance())) {
            loaderHelper.showErrorWithRetry(AppController.getResourses().getString(R.string.txt_internet_error));
            return;
        }

        loaderHelper.showLoading();

        //todo - check with local data

        //todo - network call

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                loaderHelper.dismiss();
            }
        }, 2000);

    }

    private void test() {}

}
