package pl.pnoga.weatheralert.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.dao.MeasurementDAO;
import pl.pnoga.weatheralert.app.dao.StationDAO;
import pl.pnoga.weatheralert.app.model.StationList;
import pl.pnoga.weatheralert.app.model.WeatherMeasurementList;
import pl.pnoga.weatheralert.app.request.MeasurementRequest;
import pl.pnoga.weatheralert.app.request.StationRequest;


public class WeatherAlert extends Activity {
    private final String TAG = "WeatherAlert";
    private SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);
    private StationDAO stationDAO;
    private MeasurementDAO measurementDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_alert);
        performRequestForStations();
        stationDAO = new StationDAO(this);
        measurementDAO = new MeasurementDAO(this);
        stationDAO.open();
        measurementDAO.open();
        Log.d(TAG, "Measurments " + measurementDAO.getMeasurementsCount());

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
    protected void onResume() {
        stationDAO.open();
        measurementDAO.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        stationDAO.close();
        measurementDAO.close();
        super.onPause();
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

    private void performRequestForStations() {

        StationRequest request = new StationRequest();
        spiceManager.execute(request, new StationListRequestListener());
    }

    private void performRequestForMeasurments() {
        for (String stationName : stationDAO.getStationsId()) {
            MeasurementRequest request = new MeasurementRequest(stationName);
            spiceManager.execute(request, new MeasurementRequestListener());
        }
    }

    private class StationListRequestListener implements RequestListener<StationList> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(TAG, "Failure station request");
        }

        @Override
        public void onRequestSuccess(StationList stations) {
            Log.d(TAG, "Downloaded " + stations.size() + " stations");
            stationDAO.saveStations(stations);
            performRequestForMeasurments();
        }
    }

    private class MeasurementRequestListener implements RequestListener<WeatherMeasurementList> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(TAG, "Failure measurement request");
        }

        @Override
        public void onRequestSuccess(WeatherMeasurementList weatherMeasurements) {
            Log.d(TAG, "Downloaded " + weatherMeasurements.size() + " measurement");
            measurementDAO.saveMeasurements(weatherMeasurements);

        }
    }


}
