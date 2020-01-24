package cinemo.metar.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class BindingUtils {

    private static final String VISIBILITY = "android:visibility";

    @BindingAdapter({VISIBILITY})
    public static void setVisibility(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }


}
