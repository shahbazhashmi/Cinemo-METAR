package cinemo.metar;

import android.app.Application;
import android.content.res.Resources;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class AppController extends Application {

    private static AppController mInstance;
    private static Resources mResources;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mResources = getResources();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Resources getResourses() {
        return mResources;
    }
}
