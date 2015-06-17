package pl.pnoga.weatheralert.app.synchronization;

import android.accounts.Account;
import android.content.*;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import pl.pnoga.weatheralert.app.service.LocationService;

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
        ServerDataProvider serverDataProvider = new ServerDataProvider();
        Log.d(TAG, "Station size " + serverDataProvider.getStations().size());
        Log.d(TAG, "Measurment size " + serverDataProvider.getWeatherMeasurements().size());
        updateStations();
        Location location = updateLocation();
        Log.d(TAG, "Lon " + location.getLongitude());
        Log.d(TAG, "Lat " + location.getLatitude());
    }

    private void updateStations() {

    }

    private Location updateLocation() {
        LocationService locationService = new LocationService(getContext());
        return locationService.getLocation();
    }

    private void getDataForLocation(Location location) {

    }

    private void notifyUser() {

    }


}
