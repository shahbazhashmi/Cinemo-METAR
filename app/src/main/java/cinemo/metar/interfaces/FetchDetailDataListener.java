package cinemo.metar.interfaces;

import cinemo.metar.room.Station;

/**
 * Created by Shahbaz Hashmi on 2020-02-01.
 */
public interface FetchDetailDataListener {

    void onSuccess(Station station);

    void onUpdatedData(Station station);

    void onError(String errMsg, boolean canRetry);

    void onErrorPrompt(String errMsg);

}
