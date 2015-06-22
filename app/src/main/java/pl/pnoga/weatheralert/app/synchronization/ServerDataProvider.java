package pl.pnoga.weatheralert.app.synchronization;

import android.util.Log;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import pl.pnoga.weatheralert.app.model.StationList;
import pl.pnoga.weatheralert.app.model.WeatherMeasurementList;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerDataProvider {
    private final String TAG = "ServerDataProvider";


    public StationList getStations() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        StationList stations = restTemplate.getForObject("http://mech.fis.agh.edu.pl/meteo/rest/json/info/", StationList.class);
        Log.d(TAG, "Station downloaded: " + stations.size());
        return stations;
    }

    public WeatherMeasurementList getWeatherMeasurements(String stationName) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.HOUR_OF_DAY, -6);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        WeatherMeasurementList weatherMeasurements = restTemplate.getForObject("http://mech.fis.agh.edu.pl/meteo/rest/json/all/" + stationName + "/" + new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), WeatherMeasurementList.class);
        Log.d(TAG, "Measurments downloaded: " + weatherMeasurements.size() + " for station " + stationName);
        return weatherMeasurements;
    }

}
