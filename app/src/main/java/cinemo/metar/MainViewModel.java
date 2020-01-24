package cinemo.metar;

import android.app.Application;
import android.os.Handler;

import androidx.lifecycle.AndroidViewModel;

import cinemo.metar.interfaces.LoaderListener;
import cinemo.metar.loader.LoaderHelper;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class MainViewModel extends AndroidViewModel {

    public LoaderHelper loaderHelper;

    public MainViewModel(Application application) {
        super(application);
        loaderHelper = new LoaderHelper(new LoaderListener() {
            @Override
            public void onRetryClick() {

            }
        });

        loadData();
    }

    private void loadData() {
        //todo - internet check

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

}
