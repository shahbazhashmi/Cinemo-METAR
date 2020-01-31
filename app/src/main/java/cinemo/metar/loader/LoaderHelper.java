package cinemo.metar.loader;

import cinemo.metar.AppController;
import cinemo.metar.R;
import cinemo.metar.interfaces.LoaderListener;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class LoaderHelper {

    public enum States {
        LOADING, ERROR, TRY_AGAIN
    }

    public LoaderViewModel loaderViewModel;

    public boolean isLoading = false;

    public void setRetryListener(LoaderListener loaderListener) {
        loaderViewModel = new LoaderViewModel(loaderListener);
    }


    public void dismiss() {
        isLoading = false;
        loaderViewModel.dismiss();
    }

    public void showLoading() {
        isLoading = true;
        loaderViewModel.setLoaderState(States.LOADING);
        loaderViewModel.show();
    }

    public void showError(String errorText) {
        isLoading = false;
        loaderViewModel.setText(errorText);
        loaderViewModel.setLoaderState(States.ERROR);
        loaderViewModel.show();
    }

    public void showErrorWithRetry(String errorText) {
        isLoading = false;
        loaderViewModel.setText(errorText);
        loaderViewModel.setLoaderState(States.TRY_AGAIN);
        loaderViewModel.show();
    }
}
