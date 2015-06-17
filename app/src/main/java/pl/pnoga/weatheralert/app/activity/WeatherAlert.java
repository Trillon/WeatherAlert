package pl.pnoga.weatheralert.app.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.adapter.ThreatsAdapter;
import pl.pnoga.weatheralert.app.dao.MeasurementDAO;
import pl.pnoga.weatheralert.app.dao.StationDAO;
import pl.pnoga.weatheralert.app.service.LocationService;
import pl.pnoga.weatheralert.app.utils.ThreatFinder;

import static android.content.ContentResolver.setIsSyncable;


public class WeatherAlert extends Activity {
    public static final String AUTHORITY = "pl.pnoga.weatheralert.provider";
    public static final String ACCOUNT_TYPE = "pnoga.pl";
    public static final String ACCOUNT = "WeatherAlert";
    private final String TAG = "WeatherAlert";
    Account mAccount;
    TextView userCoordinates, stationCount;
    private StationDAO stationDAO;
    private MeasurementDAO measurementDAO;

    private static Account CreateSyncAccount(Context context) {
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

    private static String locationStringFromLocation(final Location location) {
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " " + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
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
        stationDAO = new StationDAO(this);
        measurementDAO = new MeasurementDAO(this);
        stationDAO.open();
        measurementDAO.open();
        LocationService locationService = new LocationService(this);
        Location location = locationService.getLocation();
        userCoordinates = (TextView) findViewById(R.id.txt_user_coordinates);
        stationCount = (TextView) findViewById(R.id.txt_station_count);
        userCoordinates.setText("Obecna lokacja:\n" + locationStringFromLocation(location));
        stationCount.setText("Ilość stacji w zasiegu: " + stationDAO.getStations().size());
        ListView threats = (ListView) findViewById(R.id.lv_threats);
        ThreatsAdapter threatsAdapter = new ThreatsAdapter(this, ThreatFinder.getThreats());
        threats.setAdapter(threatsAdapter);


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
}
