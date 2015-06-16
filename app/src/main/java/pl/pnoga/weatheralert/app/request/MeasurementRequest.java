package pl.pnoga.weatheralert.app.request;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import pl.pnoga.weatheralert.app.model.WeatherMeasurementList;

public class MeasurementRequest extends SpringAndroidSpiceRequest<WeatherMeasurementList> {

    private String stationName;

    public MeasurementRequest(String stationName) {
        super(WeatherMeasurementList.class);
        this.stationName = stationName;
    }

    @Override
    public WeatherMeasurementList loadDataFromNetwork() throws Exception {
        String url = "http://mech.fis.agh.edu.pl/meteo/rest/json/last/" + stationName;
        return getRestTemplate().getForObject(url, WeatherMeasurementList.class);
    }
}
