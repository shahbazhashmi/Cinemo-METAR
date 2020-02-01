package cinemo.metar.room;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import cinemo.metar.AppController;
import cinemo.metar.Config;
import cinemo.metar.R;
import cinemo.metar.interfaces.FetchDetailDataListener;
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

    private boolean InsertUpdated(Station station) {
            if(mStationDao.getStationByData(station.getFileName(), station.getDateModified(), String.valueOf(station.getSize())) == null) {
                mStationDao.insertStation(station);
                return true;
            } else {
                //Log.d(TAG, "identical record found -> "+station.getFileName());
                return false;
            }
    }

    public void update(Station station) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.updateStation(station);
        });
    }

    /**
     * update station if new data found
     * @param station
     */
    private boolean updateUpdated(Station station) {
            Station oldStation = mStationDao.getStationByFileName(station.getFileName());
            if(TextUtils.isEmpty(oldStation.getData()) || !oldStation.getData().equals(station.getData())) {
                mStationDao.updateStation(station);
                return true;
            } else {
                return false;
            }
    }

    public LiveData<List<Station>> getAllStations() {
        return mStationDao.getAllStations();
    }

    public void deleteAll() {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mStationDao.deleteAllStations();
        });
    }

    public Station getStationByFileName(String fileName) {
        return mStationDao.getStationByFileName(fileName);
    }


    public void fetchAndGetListData(FetchListDataListener fetchDataListener) {

        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            boolean isDataExist = false;
            try {
                Log.d(TAG, "fetchAndGetListData -> **** start ****");
                isDataExist = isListDataExist();

                /**
                 * if data exist, return to avoid waiting time
                 */
                if(isDataExist) {
                    DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                        fetchDataListener.onSuccess(getAllStations());
                    });
                    Log.d(TAG, "fetchAndGetListData -> publishing existing data");
                }

                if(!AppUtils.isNetworkAvailable(AppController.getInstance())) {
                    if(isDataExist) {
                        /**
                         *  just prompt user, as there are already data to show
                         */
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                            fetchDataListener.onErrorPrompt(AppController.getResourses().getString(R.string.txt_internet_error));
                        });
                        Log.d(TAG, "fetchAndGetListData -> internet error prompt - publishing existing data");
                    } else {
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                                    fetchDataListener.onError(AppController.getResourses().getString(R.string.txt_internet_error), true);
                                });
                        Log.d(TAG, "fetchAndGetListData -> internet error - showing error");
                    }
                    return;
                }



                String requestURL = Config.METAR_DECODED_URL;
                Log.d(TAG, "fetchAndGetListData -> calling - "+requestURL);
                HttpUtility.sendGetRequest(requestURL);
                String[] response = HttpUtility.readMultipleLinesRespone();

                if(response == null || response.length == 0) {
                    throw new NullPointerException("data not found");
                }

                /**
                 * parsing HTML to insert in station table
                 */
                boolean insertFlag = false;
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
                        /*Log.d(TAG, "file_name -> "+fileName);
                        Log.d(TAG, "date_time -> "+dateTime);
                        Log.d(TAG, "size -> "+size);*/

                        boolean isInserted = InsertUpdated(new Station(fileName, dateTime, Long.parseLong(size)));

                        /**
                         * mark insertFlag true to know even single record is inserted
                         */
                        if(!insertFlag && isInserted) {
                            insertFlag = true;
                        }
                    }
                }

                if(insertFlag) {
                    if(isDataExist) {
                        /**
                         * just notify that new record found as there are data already
                         */
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                            fetchDataListener.onUpdatedData(getAllStations());
                        });
                        Log.d(TAG, "fetchAndGetListData -> publishing update");
                    } else {
                        /**
                         * return new list as there is no data
                         */
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                            fetchDataListener.onSuccess(getAllStations());
                        });
                        Log.d(TAG, "fetchAndGetListData -> publishing new data");
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                if(isDataExist) {
                    /**
                     *  just prompt user, as there are already data to show
                     */
                    DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                                fetchDataListener.onErrorPrompt(AppController.getResourses().getString(R.string.txt_something_went_wrong));
                            });
                    Log.d(TAG, "fetchAndGetListData -> exception - showing error prompt");
                } else {
                    DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                                fetchDataListener.onError(AppController.getResourses().getString(R.string.txt_something_went_wrong), true);
                            });
                    Log.d(TAG, "fetchAndGetListData -> exception - showing error");
                }
            } finally {
                HttpUtility.disconnect();
                Log.d(TAG, "fetchAndGetListData -> **** end ****");
            }
        });
    }


    public void fetchAndGetDetailData(Station station, FetchDetailDataListener fetchDataListener) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            boolean isDataExist = false;
            try {
                Log.d(TAG, "fetchAndGetDetailData -> **** start ****");
                isDataExist = !TextUtils.isEmpty(station.getData());

                /**
                 * if data exist, return to avoid waiting time
                 */
                if(isDataExist) {
                    DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                        fetchDataListener.onSuccess(station);
                    });
                    Log.d(TAG, "fetchAndGetDetailData -> publishing existing data");
                }

                if(!AppUtils.isNetworkAvailable(AppController.getInstance())) {
                    if(isDataExist) {
                        /**
                         *  just prompt user, as there are already data to show
                         */
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                                    fetchDataListener.onErrorPrompt(AppController.getResourses().getString(R.string.txt_internet_error));
                                });
                        Log.d(TAG, "fetchAndGetDetailData -> internet error prompt - publishing existing data");
                    } else {
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                                    fetchDataListener.onError(AppController.getResourses().getString(R.string.txt_internet_error), true);
                                });
                        Log.d(TAG, "fetchAndGetDetailData -> internet error - showing error");
                    }
                    return;
                }



                String requestURL = Config.METAR_DECODED_URL+station.getFileName();
                Log.d(TAG, "fetchAndGetDetailData -> calling - "+requestURL);
                HttpUtility.sendGetRequest(requestURL);
                String[] response = HttpUtility.readMultipleLinesRespone();

                if(response == null || response.length == 0) {
                    throw new NullPointerException("data not found");
                }

                /**
                 * parsing HTML to insert in station table
                 */
                StringBuilder stringBuilder = new StringBuilder();
                for (String line : response)  {
                    stringBuilder.append(line);
                    stringBuilder.append("\n\n");
                }

                station.setData(stringBuilder.toString());

                boolean isUpdated = updateUpdated(station);

                if(isUpdated) {
                    if(isDataExist) {
                        /**
                         * just notify that new data found as there are data already
                         */
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                            fetchDataListener.onUpdatedData(station);
                        });
                        Log.d(TAG, "fetchAndGetDetailData -> publishing update");
                    } else {
                        /**
                         * return station as there is no data
                         */
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                            fetchDataListener.onSuccess(station);
                        });
                        Log.d(TAG, "fetchAndGetDetailData -> publishing existing data");
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                if(isDataExist) {
                    /**
                     *  just prompt user, as there are already data to show
                     */
                    DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                                fetchDataListener.onErrorPrompt(AppController.getResourses().getString(R.string.txt_something_went_wrong));
                            });
                    Log.d(TAG, "fetchAndGetDetailData -> exception - showing error prompt");
                } else {
                    DefaultExecutorSupplier.getInstance().forMainThreadTasks().execute(() -> {
                                fetchDataListener.onError(AppController.getResourses().getString(R.string.txt_something_went_wrong), true);
                            });
                    Log.d(TAG, "fetchAndGetDetailData -> exception - showing error");
                }
            } finally {
                HttpUtility.disconnect();
                Log.d(TAG, "fetchAndGetDetailData -> **** end ****");
            }
        });
    }


    public boolean isListDataExist() {
        return mStationDao.getCount() > 0;
    }

    public boolean isStationDataExist(String fileName) {
       return  !TextUtils.isEmpty(getStationByFileName(fileName).getData());
    }




}
