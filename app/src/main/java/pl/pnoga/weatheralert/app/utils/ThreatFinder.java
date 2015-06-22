package pl.pnoga.weatheralert.app.utils;

import android.location.Location;
import pl.pnoga.weatheralert.app.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThreatFinder {
    private static final double CRIT_MAX_TEMPERATURE = 25.0;
    private static final double CRIT_MIN_TEMPERATURE = -15.0;
    private static final double CRIT_WIND_SPEED = 25.0;
    private static final double CRIT_SHOWER = 30.0;
    private static final double MAX_RADIUS = 20.0;
    private static final double MAX_CLOSE_RADIUS = 10.0;

    public static ThreatList getThreatsForStation(WeatherMeasurementList weatherMeasurements, Station station, boolean close) {
        ThreatList threats = new ThreatList();
        if (weatherMeasurements.isEmpty()) {
            Threat threat = new Threat();
            threat.setCode(Constants.CODE_YELLOW);
            threat.setMessage("Nie znaleziono aktualnych pomiarów dla stacji:");
            threat.setStation(station);
            threat.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            threats.add(threat);
        } else {
        threats.addAll(getWindThreat(weatherMeasurements, station, close));
        threats.addAll(getShowerThreat(weatherMeasurements, station, close));
        threats.addAll(getStormThreat(weatherMeasurements, station, close));
        threats.addAll(getTemparatureThreat(weatherMeasurements, station, close));
        if (threats.size() == 0) {
            Threat threat = new Threat();
            threat.setCode(Constants.CODE_GREEN);
            threat.setMessage("Brak zagrożen ze stacji:");
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

    public static List<String> getStationsInRadius(StationList stations, Location location, boolean close) {
        List<String> stationNames = new ArrayList<>();
        double myLatitude = location.getLatitude(), myLongitude = location.getLongitude();
        if (close) {
            for (Station station : stations) {
                if (Haversine.haversine(myLatitude, myLongitude, station.getLati(), station.getLongi()) < MAX_CLOSE_RADIUS)
                    stationNames.add(station.getStation());
            }
        } else {
            for (Station station : stations) {
                double distance = Haversine.haversine(myLatitude, myLongitude, station.getLati(), station.getLongi());
                if ((distance < MAX_RADIUS) && (distance >= MAX_CLOSE_RADIUS))
                    stationNames.add(station.getStation());
            }
        }

        return stationNames;

    }

    public static List<String> getAllStationInRadius(StationList stations, Location location) {
        List<String> stationNames = new ArrayList<>();
        double myLatitude = location.getLatitude(), myLongitude = location.getLongitude();
        for (Station station : stations) {
            if (Haversine.haversine(myLatitude, myLongitude, station.getLati(), station.getLongi()) < MAX_RADIUS)
                stationNames.add(station.getStation());
        }

        return stationNames;

    }

    private static ThreatList getWindThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close) {
        ThreatList threats = new ThreatList();
        Threat threat = new Threat();
        double maxWindSpeed = 0.0;

        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getWindSpeed() > CRIT_WIND_SPEED) {
                maxWindSpeed = weatherMeasurement.getData().getWindSpeed();
                threat.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threat.setTime(weatherMeasurement.getTime());
                threat.setMessage("Niebezpieczna prędkośc wiatru: " + maxWindSpeed);
                threat.setStation(station);
            }
        }
        if (maxWindSpeed > CRIT_WIND_SPEED)
            threats.add(threat);
        return threats;
    }

    private static ThreatList getShowerThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close) {
        ThreatList threats = new ThreatList();
        Threat threat = new Threat();
        double shower = 0.0;

        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getLastHourDrop() > CRIT_SHOWER) {
                shower = weatherMeasurement.getData().getLastHourDrop();
                threat.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threat.setTime(weatherMeasurement.getTime());
                threat.setMessage("Obfite opady: " + shower);
                threat.setStation(station);
            }
        }
        if (shower > CRIT_SHOWER)
            threats.add(threat);
        return threats;
    }

    private static ThreatList getStormThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close) {
        ThreatList threats = new ThreatList();
        return threats;
    }

    private static ThreatList getTemparatureThreat(WeatherMeasurementList weatherMeasurements, Station station, boolean close) {
        ThreatList threats = new ThreatList();
        double minTemp = 0.0, maxTemp = 0.0;
        Threat threatMin = new Threat();
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getTemperature() < CRIT_MIN_TEMPERATURE) {
                minTemp = weatherMeasurement.getData().getTemperature();
                threatMin.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threatMin.setTime(weatherMeasurement.getTime());
                threatMin.setMessage("Niska temperatura: " + minTemp);
                threatMin.setStation(station);
            }
        }
        if (minTemp < CRIT_MIN_TEMPERATURE)
            threats.add(threatMin);

        Threat threatMax = new Threat();
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (weatherMeasurement.getData().getTemperature() > CRIT_MAX_TEMPERATURE) {
                maxTemp = weatherMeasurement.getData().getTemperature();
                threatMax.setCode(close ? Constants.CODE_RED : Constants.CODE_YELLOW);
                threatMax.setTime(weatherMeasurement.getTime());
                threatMax.setMessage("Wysoka temperatura: " + maxTemp);
                threatMax.setStation(station);
            }
        }
        if (maxTemp > CRIT_MAX_TEMPERATURE)
            threats.add(threatMax);
        return threats;
    }
}
