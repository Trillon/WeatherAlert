package pl.pnoga.weatheralert.app.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.adapter.ThreatsAdapter;
import pl.pnoga.weatheralert.app.dao.MeasurementDAO;
import pl.pnoga.weatheralert.app.dao.StationDAO;
import pl.pnoga.weatheralert.app.dao.ThreatDAO;
import pl.pnoga.weatheralert.app.model.WeatherMeasurement;
import pl.pnoga.weatheralert.app.service.LocationService;
import pl.pnoga.weatheralert.app.utils.ThreatComparator;
import pl.pnoga.weatheralert.app.utils.ThreatFinder;

import static android.content.ContentResolver.setIsSyncable;


public class WeatherAlert extends Activity {
    public static final String AUTHORITY = "pl.pnoga.weatheralert.provider";
    public static final String ACCOUNT_TYPE = "pnoga.pl";
    public static final String ACCOUNT = "WeatherAlert";
    public static final String ACTION_FINISHED_SYNC = "pl.pnoga.weatheralert.app.activity.ACTION_FINISHED_SYNC";
    private static IntentFilter syncIntentFilter = new IntentFilter(ACTION_FINISHED_SYNC);
    private final int SYNC_INTERVAL = 30 * 60;
    private final String TAG = "WeatherAlert";
    Account mAccount;
    TextView userCoordinates, stationCount;
    ThreatsAdapter threatsAdapter;
    private StationDAO stationDAO;
    private MeasurementDAO measurementDAO;
    private ThreatDAO threatDAO;
    private ProgressDialog progressDialog;
    private LocationService locationService;
    private BroadcastReceiver syncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
            progressDialog.dismiss();
        }
    };

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
        createUserAndStartUpdates();
        openDatabaseConnections();
        getLocationAndUpdateView();
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
        openDatabaseConnections();
        super.onResume();
        registerReceiver(syncBroadcastReceiver, syncIntentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(syncBroadcastReceiver);
        closeDatabaseConnections();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sync_settings) {
            progressDialog.setMessage("Trwa synchronizacja...");
            progressDialog.show();
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
            return true;
        }
        if (id == R.id.options_settings) {
            startActivity(new Intent(this, OptionsActivity.class));
            return true;
        }
        if (id == R.id.info_settings) {
            final Dialog dialog = new Dialog(WeatherAlert.this);
            dialog.setContentView(R.layout.about_dialog);
            dialog.setTitle("O aplikacji");
            TextView about = (TextView) dialog.findViewById(R.id.txt_about);
            about.setText(R.string.about);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateView() {
        threatsAdapter.clear();
        threatsAdapter.addAll(threatDAO.getAllThreats());
        threatsAdapter.sort(new ThreatComparator());
        threatsAdapter.notifyDataSetChanged();
        stationCount.setText("Ilość stacji w zasiegu: " + ThreatFinder.getAllStationInRadius(stationDAO.getStations(), locationService.getLocation(), this).size());
    }

    private void openDatabaseConnections() {
        stationDAO = new StationDAO(this);
        measurementDAO = new MeasurementDAO(this);
        threatDAO = new ThreatDAO(this);
        stationDAO.open();
        measurementDAO.open();
        threatDAO.open();
    }

    private void closeDatabaseConnections() {
        stationDAO.close();
        measurementDAO.close();
        threatDAO.close();
    }

    private void createUserAndStartUpdates() {
        mAccount = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.setIsSyncable(mAccount, AUTHORITY, 1);
        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    private void getLocationAndUpdateView() {
        locationService = new LocationService(this);
        Location location = locationService.getLocation();
        userCoordinates = (TextView) findViewById(R.id.txt_user_coordinates);
        stationCount = (TextView) findViewById(R.id.txt_station_count);
        String s = "Obecna lokacja:\n" + locationStringFromLocation(location);
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 16, s.length(), 0);
        userCoordinates.setText(ss1);
        ListView threats = (ListView) findViewById(R.id.lv_threats);
        threatsAdapter = new ThreatsAdapter(this, threatDAO.getAllThreats());
        threats.setAdapter(threatsAdapter);
        threats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherMeasurement weatherMeasurement = measurementDAO.getMeasurmentsForStation(threatsAdapter.getItem(position).getStation().getStation());
                final Dialog dialog = new Dialog(WeatherAlert.this);
                if (weatherMeasurement != null) {
                    dialog.setContentView(R.layout.details_dialog);
                    dialog.setTitle("Ostatni pomiar");
                    TextView time = (TextView) dialog.findViewById(R.id.dialog_time);
                    TextView temperature = (TextView) dialog.findViewById(R.id.dialog_temperature);
                    TextView pressure = (TextView) dialog.findViewById(R.id.dialog_pressure);
                    TextView moisture = (TextView) dialog.findViewById(R.id.dialog_moisture);
                    TextView shower = (TextView) dialog.findViewById(R.id.dialog_shower);
                    TextView wind_speed = (TextView) dialog.findViewById(R.id.dialog_wind_speed);
                    time.setText(weatherMeasurement.getTime());
                    temperature.setText(weatherMeasurement.getData().getTemperature() + " °C");
                    pressure.setText(weatherMeasurement.getData().getPressure() + " hPa");
                    moisture.setText(weatherMeasurement.getData().getMoisture() + " %");
                    shower.setText(weatherMeasurement.getData().getShowers() + " mm");
                    wind_speed.setText(weatherMeasurement.getData().getWindSpeed() + " m/s");
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialog_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    dialog.setTitle("Brak pomiarów dla stacji");
                }
                dialog.show();
            }
        });
        progressDialog = new ProgressDialog(this);
        updateView();
    }

}
