package pl.pnoga.weatheralert.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import pl.pnoga.weatheralert.app.model.StationList;


public class WeatherAlert extends Activity {
    private final String TAG = "WeatherAlert";
    private SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);
    private LocationManager mLocationManager;
    private final long LOCATION_REFRESH_TIME = 1000 *60;
    private final long LOCATION_REFRESH_DISTANCE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_alert);
        performRequest();
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location;
        double longitude = 0, latitude=0;
        if(network_enabled){
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null){
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
        }
        Log.d(TAG, String.valueOf(longitude));
        Log.d(TAG, String.valueOf(latitude));
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void performRequest() {

        StationRequest request = new StationRequest();
        spiceManager.execute(request, new StationListRequestListener());
    }
    private class StationListRequestListener implements RequestListener<StationList>{
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(TAG, "Failure");
        }

        @Override
        public void onRequestSuccess(StationList stations) {
            Log.d(TAG, String.valueOf(stations.size()));
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d(TAG, String.valueOf(location.getLatitude()));
            Log.d(TAG, String.valueOf(location.getLongitude()));
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
    };
}
