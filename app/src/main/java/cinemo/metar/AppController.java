package cinemo.metar;

import android.app.Application;
import android.content.res.Resources;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class AppController extends Application {

    private static AppController mInstance;
    public static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Resources getResourses() {
        return res;
    }
}
