package pl.pnoga.weatheralert.app.utils;

import android.content.Context;
import android.location.Location;
import pl.pnoga.weatheralert.app.dao.OptionsDAO;
import pl.pnoga.weatheralert.app.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThreatFinder {

    public static ThreatList getThreatsForStation(WeatherMeasurementList weatherMeasurements, Station station, boolean close, Context context) {
        ThreatList threats = new ThreatList();
        if (weatherMeasurements.isEmpty()) {
            Threat threat = new Threat();
            threat.setCode(Constants.CODE_YELLOW);
            threat.setMessage("Nie znaleziono aktualnych pomiarów dla stacji:");
            threat.setStation(station);
            threat.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            threats.add(threat);
        } else {
            threats.addAll(getWindThreat(weatherMeasurements, station, close, context));
            threats.addAll(getShowerThreat(weatherMeasurements, station, close, context));
        threats.addAll(getStormThreat(weatherMeasurements, station, close));
            threats.addAll(getTemparatureThreat(weatherMeasurements, station, close, context));
        if (threats.size() == 0) {
            Threat threat = new Threat();
            threat.setCode(Constants.CODE_GREEN);
            threat.setMessage("Brak zagrożeń ze stacji:");
            threat.setStation(station);
            threat.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLatestDate(weatherMeasurements)));
            threats.add(threat);
        }
        }
        return threats;
    }

    private static Date getLatestDate(WeatherMeasurementList weatherMeasurements) {
        Date returnDate = new Date(0);
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            Date currentDate = new Date(0);
            try {
                currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(weatherMeasurement.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (returnDate.before(currentDate)) {
                returnDate = currentDate;
            }
        }
        return returnDate;
    }

    public static List<String> getStationsInRadius(StationList stations, Location location, boolean close, Context context) {
        List<String> stationNames = new ArrayList<>();
        OptionsDAO optionsDAO = new OptionsDAO(context);
        optionsDAO.open();
        double maxRadius = optionsDAO.getMaxRadius();
        double closeRadius = optionsDAO.getCloseRadius();
        optionsDAO.close();
        double myLatitude = location.getLatitude(), myLongitude = location.getLongitude();
        if (close) {
            for (Station station : stations) {
                if (Haversine.getHaversineValue(myLatitude, myLongitude, station.getLati(), station.getLongi()) < closeRadius)
                    stationNames.add(station.getStation());
            }
        } else {
            for (Station station : stations) {
                double distance = Haversine.getHaversineValue(myLatitude, myLongitude, station.getLati(), station.getLongi());
                if ((distance < maxRadius) && (distance >= closeRadius))
                    stationNames.add(station.getStation());
            }
        }
        return stationNames;

    }

    public static List<String> getAllStationInRadius(StationList stations, Location location, Context context) {
        List<String> stationNames = new ArrayList<>();
        OptionsDAO optionsDAO = new OptionsDAO(context);
        optionsDAO.open();
        double maxRadius = optionsDAO.getMaxRadius();
        optionsDAO.close();
        double myLatitude = location.getLatitude(), myLongitude = location.getLongitude();
        for (Station station : stations) {
            if (Haversine.getHaversineValue(myLatitude, myLongitude, station.getLati(), station.getLongi()) < maxRadius)
                stationNames.add(station.getStation());
        }
        return stationNames;

    }

    private static ThreatList getWindThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close, Context context) {
        ThreatList threats = new ThreatList();
        Threat threat = new Threat();
        double maxWindSpeed = 0.0;
        OptionsDAO optionsDAO = new OptionsDAO(context);
        optionsDAO.open();
        double critWindSpeed = optionsDAO.getCritWindSpeed();
        optionsDAO.close();
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getWindSpeed() > critWindSpeed) {
                maxWindSpeed = weatherMeasurement.getData().getWindSpeed();
                threat.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threat.setTime(weatherMeasurement.getTime());
                threat.setMessage("Niebezpieczna prędkość wiatru: " + maxWindSpeed);
                threat.setStation(station);
            }
        }
        if (maxWindSpeed > critWindSpeed)
            threats.add(threat);
        return threats;
    }

    private static ThreatList getShowerThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close, Context context) {
        ThreatList threats = new ThreatList();
        Threat threat = new Threat();
        double shower = 0.0;
        OptionsDAO optionsDAO = new OptionsDAO(context);
        optionsDAO.open();
        double critShower = optionsDAO.getCritShower();
        optionsDAO.close();
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getLastHourDrop() > critShower) {
                shower = weatherMeasurement.getData().getLastHourDrop();
                threat.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threat.setTime(weatherMeasurement.getTime());
                threat.setMessage("Obfite opady: " + shower);
                threat.setStation(station);
            }
        }
        if (shower > critShower)
            threats.add(threat);
        return threats;
    }

    private static ThreatList getStormThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close) {
        ThreatList threats = new ThreatList();
        return threats;
    }

    private static ThreatList getTemparatureThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close, Context context) {
        ThreatList threats = new ThreatList();
        double minTemp = 0.0, maxTemp = 0.0;
        OptionsDAO optionsDAO = new OptionsDAO(context);
        optionsDAO.open();
        double critMinTemperature = optionsDAO.getMinCritTemperature();
        double critMaxTemperature = optionsDAO.getMaxCritTemperature();
        optionsDAO.close();
        Threat threatMin = new Threat();
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getTemperature() < critMinTemperature) {
                minTemp = weatherMeasurement.getData().getTemperature();
                threatMin.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threatMin.setTime(weatherMeasurement.getTime());
                threatMin.setMessage("Niska temperatura: " + minTemp);
                threatMin.setStation(station);
            }
        }
        if (minTemp < critMinTemperature)
            threats.add(threatMin);

        Threat threatMax = new Threat();
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getTemperature() > critMaxTemperature) {
                maxTemp = weatherMeasurement.getData().getTemperature();
                threatMax.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threatMax.setTime(weatherMeasurement.getTime());
                threatMax.setMessage("Wysoka temperatura: " + maxTemp);
                threatMax.setStation(station);
            }
        }
        if (maxTemp > critMaxTemperature)
            threats.add(threatMax);
        return threats;
    }
}
