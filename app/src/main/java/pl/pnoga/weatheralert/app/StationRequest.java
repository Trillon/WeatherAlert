package pl.pnoga.weatheralert.app;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import pl.pnoga.weatheralert.app.model.StationList;

public class StationRequest extends SpringAndroidSpiceRequest<StationList> {

    public StationRequest() {
        super(StationList.class);
    }

    @Override
    public StationList loadDataFromNetwork() throws Exception {
        String url = String.format("http://mech.fis.agh.edu.pl/meteo/rest/json/info/");

        return getRestTemplate().getForObject(url, StationList.class);
    }
}
