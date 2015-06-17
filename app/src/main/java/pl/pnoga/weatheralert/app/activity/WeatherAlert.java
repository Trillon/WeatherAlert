package pl.pnoga.weatheralert.app.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.dao.MeasurementDAO;
import pl.pnoga.weatheralert.app.dao.StationDAO;
import pl.pnoga.weatheralert.app.service.LocationService;

import static android.content.ContentResolver.setIsSyncable;


public class WeatherAlert extends Activity {
    public static final String AUTHORITY = "pl.pnoga.weatheralert.provider";
    public static final String ACCOUNT_TYPE = "pnoga.pl";
    public static final String ACCOUNT = "WeatherAlert";
    private final String TAG = "WeatherAlert";
    Account mAccount;

    private StationDAO stationDAO;
    private MeasurementDAO measurementDAO;

    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            setIsSyncable(newAccount, AUTHORITY, 1);
        }
        return newAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_alert);
        mAccount = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.setIsSyncable(mAccount, AUTHORITY, 1);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
        //performRequestForStations();
        stationDAO = new StationDAO(this);
        measurementDAO = new MeasurementDAO(this);
        stationDAO.open();
        measurementDAO.open();
        LocationService locationService = new LocationService(this);
        Location location = locationService.getLocation();
        Log.d(TAG, "Lon " + location.getLongitude());
        Log.d(TAG, "Lat " + location.getLatitude());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
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


}
