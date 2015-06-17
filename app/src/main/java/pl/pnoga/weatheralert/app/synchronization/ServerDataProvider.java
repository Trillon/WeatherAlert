package pl.pnoga.weatheralert.app.synchronization;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import pl.pnoga.weatheralert.app.model.StationList;
import pl.pnoga.weatheralert.app.model.WeatherMeasurementList;

public class ServerDataProvider {
    private final String TAG = "ServerDataProvider";


    public StationList getStations() {
        StationList stations = new StationList();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        stations = restTemplate.getForObject("http://mech.fis.agh.edu.pl/meteo/rest/json/info/", StationList.class);
        return stations;
    }

    public WeatherMeasurementList getWeatherMeasurements() {
        WeatherMeasurementList weatherMeasurements = new WeatherMeasurementList();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        weatherMeasurements = restTemplate.getForObject("http://mech.fis.agh.edu.pl/meteo/rest/json/last/s000", WeatherMeasurementList.class);
        return weatherMeasurements;
    }

}
