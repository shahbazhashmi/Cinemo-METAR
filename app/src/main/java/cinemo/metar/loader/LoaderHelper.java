package cinemo.metar.loader;

import cinemo.metar.interfaces.LoaderListener;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class LoaderHelper {

    public enum States {
        LOADING, ERROR, TRY_AGAIN
    }

    LoaderViewModel loaderViewModel;

    public LoaderHelper(LoaderListener loaderListener) {
        loaderViewModel = new LoaderViewModel(loaderListener);
    }


    public void dismiss() {
        loaderViewModel.dismiss();
    }

    public void showLoading() {
        loaderViewModel.setLoaderState(States.LOADING);
        loaderViewModel.show();
    }

    public void showError(String errorText) {
        loaderViewModel.setText(errorText);
        loaderViewModel.setLoaderState(States.ERROR);
        loaderViewModel.show();
    }

    public void showErrorWithRetry(String errorText) {
        loaderViewModel.setText(errorText);
        loaderViewModel.setLoaderState(States.TRY_AGAIN);
        loaderViewModel.show();
    }
}
