package pl.pnoga.weatheralert.app.synchronization;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.*;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.dao.MeasurementDAO;
import pl.pnoga.weatheralert.app.dao.StationDAO;
import pl.pnoga.weatheralert.app.model.Station;
import pl.pnoga.weatheralert.app.model.StationList;
import pl.pnoga.weatheralert.app.service.LocationService;
import pl.pnoga.weatheralert.app.utils.Haversine;

import java.util.ArrayList;
import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = "SyncAdapter";
    ContentResolver mContentResolver;
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
        StationDAO stationDAO = new StationDAO(getContext());
        MeasurementDAO measurementDAO = new MeasurementDAO(getContext());
        measurementDAO.open();
        stationDAO.open();
        stationDAO.saveStations(serverDataProvider.getStations());
        List<String> stationsInRadius = getStationsInRadius(stationDAO.getStations(), location);
        Log.d(TAG, "Stations in radius " + stationsInRadius.size());
        for (String stationName : stationsInRadius) {
            measurementDAO.saveMeasurements(serverDataProvider.getWeatherMeasurements(stationName));
        }

        notifyUser();
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


    private void notifyUser() {
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification red = new Notification.Builder(getContext())
                .setContentTitle("Wykryto zagrożenie!!")
                .setContentText("Burza")
                .setSmallIcon(R.mipmap.red)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();
        Notification yellow = new Notification.Builder(getContext())
                .setContentTitle("Możliwe zagrożenie!!")
                .setContentText("Wiatr")
                .setSmallIcon(R.mipmap.yellow)
                .setVibrate(pattern)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true).build();
        Notification green = new Notification.Builder(getContext())
                .setContentTitle("Brak zagrożeń!!")
                .setVibrate(pattern)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.green)
                .setAutoCancel(true).build();

        notificationManager.notify(0, red);
        notificationManager.notify(1, yellow);
        notificationManager.notify(2, green);


    }


}
