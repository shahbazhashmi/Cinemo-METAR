package cinemo.metar.loader;

import cinemo.metar.AppController;
import cinemo.metar.R;
import cinemo.metar.interfaces.LoaderListener;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class LoaderHelper {

    public enum States {
        LOADING, ERROR, TRY_AGAIN, EMPTY_SEARCH, SHOWING_DATA
    }

    public LoaderViewModel loaderViewModel;

    private States mLoaderState;

    public void setRetryListener(LoaderListener loaderListener) {
        loaderViewModel = new LoaderViewModel(loaderListener);
    }


    public void dismiss() {
        mLoaderState = States.SHOWING_DATA;
        loaderViewModel.dismiss();
    }

    public void showLoading() {
        mLoaderState = States.LOADING;
        loaderViewModel.setLoaderState(States.LOADING);
        loaderViewModel.show();
    }

    public void showError(String errorText) {
        mLoaderState = States.ERROR;
        loaderViewModel.setText(errorText);
        loaderViewModel.setLoaderState(States.ERROR);
        loaderViewModel.show();
    }

    public void showErrorWithRetry(String errorText) {
        mLoaderState = States.ERROR;
        loaderViewModel.setText(errorText);
        loaderViewModel.setLoaderState(States.TRY_AGAIN);
        loaderViewModel.show();
    }

    public void showEmptySearch() {
        mLoaderState = States.EMPTY_SEARCH;
        loaderViewModel.setText(AppController.getResourses().getString(R.string.txt_search_data_not_found));
        loaderViewModel.setLoaderState(States.ERROR);
        loaderViewModel.show();
    }

    public States getLoaderState() {
        return mLoaderState;
    }

    public boolean isShowingData() {
        return mLoaderState == States.SHOWING_DATA || mLoaderState == States.LOADING || mLoaderState == States.EMPTY_SEARCH;
    }

    /**
     * use this for only API call
     * @return
     */
    public boolean isLoading() {
        return mLoaderState == States.LOADING;
    }
}
