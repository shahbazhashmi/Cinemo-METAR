package cinemo.metar.loader;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import cinemo.metar.interfaces.LoaderListener;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class LoaderViewModel extends ViewModel {

    private LoaderListener mLoaderListener;

    LoaderViewModel(LoaderListener loaderListener) {
        mLoaderListener = loaderListener;
    }

    public ObservableField loaderState = new ObservableField<LoaderHelper.States>();
    public ObservableField text = new ObservableField<String>();
    public ObservableBoolean isVisible = new ObservableBoolean(false);

    void setLoaderState(LoaderHelper.States loaderState) {
        this.loaderState.set(loaderState);
    }

    void setText(String text) {
        this.text.set(text);
    }

    void show() {
        isVisible.set(true);
    }

    void dismiss() {
        isVisible.set(true);
    }

    public void onRetryClick(View view) {
        mLoaderListener.onRetryClick();
    }

}
