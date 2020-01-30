package cinemo.metar.interfaces;

import androidx.lifecycle.LiveData;

import java.util.List;

import cinemo.metar.room.Station;

/**
 * Created by Shahbaz Hashmi on 2020-01-30.
 */
public interface FetchListDataListener {

    void onSuccess(LiveData<List<Station>> stationList);

    void onError(String errMsg, boolean canRetry);

}
