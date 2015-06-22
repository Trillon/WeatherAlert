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
import pl.pnoga.weatheralert.app.model.Threat;
import pl.pnoga.weatheralert.app.model.ThreatList;
import pl.pnoga.weatheralert.app.service.LocationService;
import pl.pnoga.weatheralert.app.utils.Constants;
import pl.pnoga.weatheralert.app.utils.ThreatFinder;

import java.util.Calendar;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = "SyncAdapter";
    ContentResolver mContentResolver;
    Location location;
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
        try {
        Log.d(TAG, "performSync");
        location = updateLocation();
        ServerDataProvider serverDataProvider = new ServerDataProvider();
        stationDAO = new StationDAO(getContext());
        measurementDAO = new MeasurementDAO(getContext());
        measurementDAO.open();
        stationDAO.open();
        stationDAO.saveStations(serverDataProvider.getStations());
        for (String stationName : ThreatFinder.getAllStationInRadius(stationDAO.getStations(), location)) {
            measurementDAO.saveMeasurements(serverDataProvider.getWeatherMeasurements(stationName));
        }
        createThreatDataAndSaveToDB();
        stationDAO.close();
            measurementDAO.close();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            getContext().sendBroadcast(new Intent(WeatherAlert.ACTION_FINISHED_SYNC));
        }
        getContext().sendBroadcast(new Intent(WeatherAlert.ACTION_FINISHED_SYNC));
    }


    private Location updateLocation() {
        LocationService locationService = new LocationService(getContext());
        return locationService.getLocation();
    }

    private void createThreatDataAndSaveToDB() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.HOUR_OF_DAY, -6);
        ThreatList threats = new ThreatList();
        for (String stationName : ThreatFinder.getStationsInRadius(stationDAO.getStations(), location, true)) {
            threats.addAll(ThreatFinder.getThreatsForStation(measurementDAO.getMeasurmentsAfterDateForStation(c.getTime(), stationName), stationDAO.getStationById(stationName), true));
        }
        for (String stationName : ThreatFinder.getStationsInRadius(stationDAO.getStations(), location, false)) {
            threats.addAll(ThreatFinder.getThreatsForStation(measurementDAO.getMeasurmentsAfterDateForStation(c.getTime(), stationName), stationDAO.getStationById(stationName), false));
        }
        int alarmCount = 0, warningCount = 0;
        for (Threat threat : threats) {
            if (threat.getCode() == Constants.CODE_RED) ++alarmCount;
            if (threat.getCode() == Constants.CODE_YELLOW) ++warningCount;
        }
        threatDAO = new ThreatDAO(getContext());
        threatDAO.open();
        threatDAO.saveThreats(threats);
        threatDAO.close();
        notifyUser(alarmCount, warningCount);
    }

    private void notifyUser(int alarmCount, int warningCount) {

        Intent intent = new Intent(getContext(), WeatherAlert.class);
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (alarmCount > 0) {
        Notification red = new Notification.Builder(getContext())
                .setContentTitle("Wykryto zagrożenia (" + alarmCount + warningCount + ")!!")
                .setContentText("Kliknij aby zobaczyć")
                .setSmallIcon(R.mipmap.red)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();
            notificationManager.notify(0, red);
        } else if (warningCount > 0) {
        Notification yellow = new Notification.Builder(getContext())
                .setContentTitle("Możliwe zagrożenie (" + warningCount + ")!!")
                .setContentText("Kliknij aby zobaczyć")
                .setSmallIcon(R.mipmap.yellow)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
        notificationManager.notify(1, yellow);
        }
    }


}
