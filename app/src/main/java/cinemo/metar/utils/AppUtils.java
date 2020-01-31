package cinemo.metar.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cinemo.metar.R;

/**
 * Created by Shahbaz Hashmi on 2020-01-24.
 */
public class AppUtils {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }


    public static void showToast(final Context context, final String message, final boolean longDuration) {
        try {
            if (context instanceof Activity && ((Activity) context).isFinishing()) {
                return;
            }

            if (TextUtils.isEmpty(message) || context == null)
                return;

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(context.getApplicationContext(), message, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show());
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
