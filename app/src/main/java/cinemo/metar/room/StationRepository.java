package cinemo.metar.room;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import java.util.List;

import cinemo.metar.AppController;
import cinemo.metar.Config;
import cinemo.metar.R;
import cinemo.metar.interfaces.FetchListDataListener;
import cinemo.metar.utils.AppUtils;
import cinemo.metar.utils.HttpUtility;
import cinemo.metar.utils.StringUtils;
import cinemo.metar.utils.executors.DefaultExecutorSupplier;

/**
 * Created by Shahbaz Hashmi on 2020-01-29.
 */
public class StationRepository {

    public static final String TAG = "StationRepository";

    private StationDao mStationDao;

    StationRepository(Application application) {
        StationDatabase database = StationDatabase.getInstance(
                application.getApplicationContext());

        mStationDao = database.stationDao();
    }

    public void insert(Station station) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.insertStation(station);
        });
    }

    public void InsertUpdated(Station station) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            if(mStationDao.getStationByData(station.getFileName(), station.getDateModified(), station.getSize()) == null) {
                mStationDao.insertStation(station);
            } else {
                Log.d(TAG, "identical record found -> "+station.getFileName());
            }
        });
    }

    public void update(Station station) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.updateStation(station);
        });
    }

    public LiveData<List<Station>> getAllStations() {
        return mStationDao.getAllStations();
    }

    public void deleteAll() {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.deleteAllStations();
        });
    }


    public void fetchListData(FetchListDataListener fetchDataListener) {

        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            try {

                if(!AppUtils.isNetworkAvailable(AppController.getInstance())) {
                    if(isListDataExist()) {
                        fetchDataListener.onSuccess(getAllStations().getValue());
                    } else {
                        fetchDataListener.onError(AppController.getResourses().getString(R.string.txt_internet_error), true);
                    }
                    return;
                }

                String requestURL = Config.METAR_DECODED_URL;
                Log.d(TAG, "calling - "+requestURL);
                HttpUtility.sendGetRequest(requestURL);
                String[] response = HttpUtility.readMultipleLinesRespone();

                if(response == null || response.length == 0) {
                    throw new NullPointerException("data not found");
                }

                /**
                 * parsing HTML to insert in station table
                 */
                for (String line : response)  {
                    if(line.startsWith("<tr><td>") && line.endsWith("</td></tr>")) {
                        String[] tdArr = line.split("</td>");

                        String fileName = StringUtils.getAnchorTagText(tdArr[0]);
                        if(!(fileName.contains(".TXT") || fileName.contains(".txt"))) {
                            continue;
                        }
                        if(Config.ONLY_GERMANY_AIRPORTS && !fileName.startsWith(Config.GERMANY_AIRPORT_CODE)) {
                            continue;
                        }
                        String dateTime = StringUtils.removeHtmlTags(tdArr[1]);
                        String size = StringUtils.removeHtmlTags(tdArr[2]);
                        Log.d(TAG, "file_name -> "+fileName);
                        Log.d(TAG, "date_time -> "+dateTime);
                        Log.d(TAG, "size -> "+size);

                        InsertUpdated(new Station(fileName, dateTime, Long.parseLong(size)));
                    }
                }

                fetchDataListener.onSuccess(getAllStations().getValue());


            } catch (Exception e) {
                e.printStackTrace();
                if(isListDataExist()) {
                    fetchDataListener.onSuccess(getAllStations().getValue());
                } else {
                    fetchDataListener.onError(AppController.getResourses().getString(R.string.txt_something_went_wrong), true);
                }
            } finally {
                HttpUtility.disconnect();
            }
        });
    }

    public boolean isListDataExist() {
        return mStationDao.getCount() > 0;
    }




}
