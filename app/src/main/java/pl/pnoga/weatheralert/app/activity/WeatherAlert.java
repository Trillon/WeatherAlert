package pl.pnoga.weatheralert.app.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
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
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    private final String TAG = "WeatherAlert";
    // Instance fields
    Account mAccount;
    private SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);
    private StationDAO stationDAO;
    private MeasurementDAO measurementDAO;

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_alert);
        mAccount = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        performRequestForStations();
        stationDAO = new StationDAO(this);
        measurementDAO = new MeasurementDAO(this);
        stationDAO.open();
        measurementDAO.open();
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
