package pl.pnoga.weatheralert.app.request;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import pl.pnoga.weatheralert.app.model.StationList;

public class StationRequest extends SpringAndroidSpiceRequest<StationList> {

    public StationRequest() {
        super(StationList.class);
    }

    @Override
    public StationList loadDataFromNetwork() throws Exception {
        String url = "http://mech.fis.agh.edu.pl/meteo/rest/json/info/";

        return getRestTemplate().getForObject(url, StationList.class);
    }
}
