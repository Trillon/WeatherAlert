package pl.pnoga.weatheralert.app.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class LocationService extends Service implements LocationListener {
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    private final String TAG = "LocationService";
    private final String provider = LocationManager.NETWORK_PROVIDER;
    protected LocationManager locationManager;
    Location location;

    public LocationService() {
    }

    public LocationService(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation() {
        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider,
                    MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this, Looper.getMainLooper());
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
                return location;
            }
        } else {
            Log.d(TAG, "No provider, fixed location");
            location = new Location("");
            location.setLatitude(50.0614);
            location.setLongitude(19.9383);
            return location;
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
