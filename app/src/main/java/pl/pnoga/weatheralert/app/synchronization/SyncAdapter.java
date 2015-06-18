package pl.pnoga.weatheralert.app.synchronization;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.activity.WeatherAlert;
import pl.pnoga.weatheralert.app.dao.MeasurementDAO;
import pl.pnoga.weatheralert.app.dao.StationDAO;
import pl.pnoga.weatheralert.app.dao.ThreatDAO;
import pl.pnoga.weatheralert.app.model.*;
import pl.pnoga.weatheralert.app.service.LocationService;
import pl.pnoga.weatheralert.app.utils.Constants;
import pl.pnoga.weatheralert.app.utils.Haversine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = "SyncAdapter";
    ContentResolver mContentResolver;
    private StationDAO stationDAO;
    private MeasurementDAO measurementDAO;
    private ThreatDAO threatDAO;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {

        Log.d(TAG, "performSync");
        Location location = updateLocation();
        ServerDataProvider serverDataProvider = new ServerDataProvider();
        stationDAO = new StationDAO(getContext());
        measurementDAO = new MeasurementDAO(getContext());
        measurementDAO.open();
        stationDAO.open();
        stationDAO.saveStations(serverDataProvider.getStations());
        List<String> stationsInRadius = getStationsInRadius(stationDAO.getStations(), location);
        Log.d(TAG, "Stations in radius " + stationsInRadius.size());
        for (String stationName : stationsInRadius) {
            measurementDAO.saveMeasurements(serverDataProvider.getWeatherMeasurements(stationName));
        }
        createThreatDataAndSaveToDB();
        stationDAO.close();
        measurementDAO.close();
    }

    private List<String> getStationsInRadius(StationList stations, Location location) {
        List<String> stationNames = new ArrayList<>();
        double myLatitude = location.getLatitude(), myLongitude = location.getLongitude();
        for (Station station : stations) {
            if (Haversine.haversine(myLatitude, myLongitude, station.getLati(), station.getLongi()) < 10)
                stationNames.add(station.getStation());
        }
        return stationNames;
    }


    private Location updateLocation() {
        LocationService locationService = new LocationService(getContext());
        return locationService.getLocation();
    }

    private void createThreatDataAndSaveToDB() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_YEAR, -14);
        WeatherMeasurementList weatherMeasurements = measurementDAO.getMeasurmentsAfterDate(c.getTime());
        Log.d(TAG, "Measurment count " + weatherMeasurements.size());
        List<String> distinctStations = new ArrayList<>();
        ThreatList threats = new ThreatList();
        int i = 0;
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            if (!distinctStations.contains(weatherMeasurement.getStation())) {
                distinctStations.add(weatherMeasurement.getStation());
                Threat threat = new Threat();
                threat.setStation(stationDAO.getStationsById(weatherMeasurement.getStation()));
                threat.setTime(weatherMeasurement.getTime());
                if (i % 2 == 0) {
                    threat.setCode(Constants.CODE_RED);
                    threat.setMessage("Silny wiatr");
                } else if (i % 3 == 0) {
                    threat.setCode(Constants.CODE_YELLOW);
                    threat.setMessage("Gwałtowny spadek ciśnienia, możliwa burza");
                } else {
                    threat.setCode(Constants.CODE_GREEN);
                    threat.setMessage("Brak niebezpieczeństw");
                }
                threats.add(threat);
            }
            ++i;
        }
        Log.d(TAG, "Distinct count " + distinctStations.size());
        threatDAO = new ThreatDAO(getContext());
        threatDAO.open();
        threatDAO.saveThreats(threats);
        threatDAO.close();
        notifyUser(threats.size());
    }

    private void notifyUser(int alertCount) {

        Intent intent = new Intent(getContext(), WeatherAlert.class);
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification red = new Notification.Builder(getContext())
                .setContentTitle("Wykryto zagrożenia!!")
                .setContentText("Kliknij aby zobaczyć")
                .setSmallIcon(R.mipmap.red)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();
        Notification yellow = new Notification.Builder(getContext())
                .setContentTitle("Możliwe zagrożenie!!")
                .setContentText("Kliknij aby zobaczyć")
                .setSmallIcon(R.mipmap.yellow)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        notificationManager.notify(0, red);
        notificationManager.notify(1, yellow);

    }


}
