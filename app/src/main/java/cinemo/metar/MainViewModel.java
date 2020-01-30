package cinemo.metar;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cinemo.metar.loader.LoaderHelper;
import cinemo.metar.room.Station;
import cinemo.metar.room.StationViewModel;
import cinemo.metar.utils.AppUtils;
import cinemo.metar.utils.StringUtils;
import cinemo.metar.utils.executors.DefaultExecutorSupplier;

/**
 * Created by Shahbaz Hashmi on 2020-01-23.
 */
public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    public LoaderHelper loaderHelper;

    private StationViewModel mStationViewModel;

    public MainViewModel(Application application) {
        super(application);
        loaderHelper = new LoaderHelper(this::loadData);
        mStationViewModel = new StationViewModel(application);

        loadData();
    }

    private void loadData() {
        if(!AppUtils.isNetworkAvailable(AppController.getInstance())) {
            loaderHelper.showErrorWithRetry(AppController.getResourses().getString(R.string.txt_internet_error));
            return;
        }

        loaderHelper.showLoading();

        //todo - check with local data

        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                String urlStr = Config.METAR_DECODED_URL;
                Log.d(TAG, "calling - "+urlStr);
                URL url = new URL(urlStr);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();

                if(responseCode != HttpURLConnection.HTTP_OK) {
                    throw new Exception("failed to load page - code "+responseCode);
                }

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";

                /**
                 * parsing HTML to insert in station table
                 */
                while ((line = reader.readLine()) != null) {
                    if(line.startsWith("<tr><td>") && line.endsWith("</td></tr>")) {
                        String[] tdArr = line.split("</td>");

                        String fileName = StringUtils.getAnchorTagText(tdArr[0]);
                        if(!(fileName.contains(".TXT") || fileName.contains(".txt"))) {
                            continue;
                        }
                        String dateTime = StringUtils.removeHtmlTags(tdArr[1]);
                        String size = StringUtils.removeHtmlTags(tdArr[2]);
                        Log.d(TAG, "file_name -> "+fileName);
                        Log.d(TAG, "date_time -> "+dateTime);
                        Log.d(TAG, "size -> "+size);

                        mStationViewModel.InsertUpdatedStation(new Station(fileName, dateTime, Long.parseLong(size)));
                    }
                }

                loaderHelper.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                loaderHelper.showErrorWithRetry(AppController.getResourses().getString(R.string.txt_something_went_wrong));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
