package pl.pnoga.weatheralert.app.fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.activity.WeatherAlert;
import pl.pnoga.weatheralert.app.dao.OptionsDAO;

import static pl.pnoga.weatheralert.app.utils.Constants.AUTHORITY;

public class OptionsFragment extends PreferenceFragment {
    private final String TAG = "OptionsFragment";
    private OptionsDAO optionsDAO;
    public OptionsFragment() {
    }

    public static OptionsFragment newInstance(String param1, String param2) {
        OptionsFragment fragment = new OptionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.options);
        openDatabaseConnection();
        setDefaultOptions();
        setChangeListenersForPreferences();
    }

    @Override
    public void onDestroy() {
        optionsDAO.close();
        super.onDestroy();
    }

    private void openDatabaseConnection() {
        optionsDAO = new OptionsDAO(getActivity());
        optionsDAO.open();
    }

    private void setDefaultOptions() {
        EditTextPreference maxTempPreference = (EditTextPreference) getPreferenceScreen().findPreference("pref_max_temp");
        maxTempPreference.setText(String.valueOf(optionsDAO.getMaxCritTemperature()));

        EditTextPreference minTempPreference = (EditTextPreference) getPreferenceScreen().findPreference("pref_min_temp");
        minTempPreference.setText(String.valueOf(optionsDAO.getMinCritTemperature()));

        EditTextPreference windSpeedPreference = (EditTextPreference) getPreferenceScreen().findPreference("pref_wind_speed");
        windSpeedPreference.setText(String.valueOf(optionsDAO.getCritWindSpeed()));

        EditTextPreference showerPreference = (EditTextPreference) getPreferenceScreen().findPreference("pref_shower");
        showerPreference.setText(String.valueOf(optionsDAO.getCritShower()));

        EditTextPreference maxRadiusPreference = (EditTextPreference) getPreferenceScreen().findPreference("pref_max_radius");
        maxRadiusPreference.setText(String.valueOf(optionsDAO.getMaxRadius()));

        EditTextPreference closeRadiusPreference = (EditTextPreference) getPreferenceScreen().findPreference("pref_close_radius");
        closeRadiusPreference.setText(String.valueOf(optionsDAO.getCloseRadius()));

        EditTextPreference intervalPreference = (EditTextPreference) getPreferenceScreen().findPreference("pref_interval");
        intervalPreference.setText(String.valueOf(optionsDAO.getRefreshInterval() / 60));

        CheckBoxPreference shoeEwmpyThreatsPreference = (CheckBoxPreference) getPreferenceScreen().findPreference("pref_show_empty");
        shoeEwmpyThreatsPreference.setChecked(optionsDAO.getShowEmptyThreats() != 0);
    }

    private void setChangeListenersForPreferences() {
        getPreferenceScreen().findPreference("pref_max_temp").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean returnValue = true;
                if (Double.valueOf((String) newValue) < optionsDAO.getMinCritTemperature()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Niewłaściwe dane");
                    builder.setMessage("Maksymalna temperatura nie może być mniejsza od minimalnej!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    returnValue = false;
                } else {
                    optionsDAO.saveMaxCritTemperature(Double.valueOf((String) newValue));
                }
                return returnValue;
            }
        });
        getPreferenceScreen().findPreference("pref_min_temp").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean returnValue = true;
                if (Double.valueOf((String) newValue) > optionsDAO.getMaxCritTemperature()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Niewłaściwe dane");
                    builder.setMessage("Maksymalna temperatura nie może być mniejsza od minimalnej!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    returnValue = false;
                } else {
                    optionsDAO.saveMinCritTemperature(Double.valueOf((String) newValue));
                }
                return returnValue;
            }
        });
        getPreferenceScreen().findPreference("pref_wind_speed").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean returnValue = true;
                if (Double.valueOf((String) newValue) <= 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Niewłaściwe dane");
                    builder.setMessage("Prędkość wiatru musi być wieksza od 0!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    returnValue = false;
                } else {
                    optionsDAO.saveCritWindSpeed(Double.valueOf((String) newValue));
                }
                return returnValue;
            }
        });
        getPreferenceScreen().findPreference("pref_shower").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean returnValue = true;
                if (Double.valueOf((String) newValue) <= 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Niewłaściwe dane");
                    builder.setMessage("Opad musi być wiekszy od 0!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    returnValue = false;
                } else {
                    optionsDAO.saveCritShower(Double.valueOf((String) newValue));
                }
                return returnValue;
            }
        });
        getPreferenceScreen().findPreference("pref_max_radius").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean returnValue = true;
                if (Double.valueOf((String) newValue) < optionsDAO.getCloseRadius()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Niewłaściwe dane");
                    builder.setMessage("Maksymalny zasięg nie może być mniejszy od bliskiego zasiegu!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    returnValue = false;
                } else {
                    optionsDAO.saveMaxRadius(Double.valueOf((String) newValue));
                }
                return returnValue;
            }
        });
        getPreferenceScreen().findPreference("pref_close_radius").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean returnValue = true;
                if (Double.valueOf((String) newValue) > optionsDAO.getMaxRadius()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Niewłaściwe dane");
                    builder.setMessage("Maksymalny zasięg nie może być mniejszy od bliskiego zasiegu!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    returnValue = false;
                } else {
                    optionsDAO.saveCloseRadius(Double.valueOf((String) newValue));
                }
                return returnValue;
            }
        });
        getPreferenceScreen().findPreference("pref_interval").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean returnValue = true;
                if (Double.valueOf((String) newValue) <= 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Niewłaściwe dane");
                    builder.setMessage("Interwał odświezania musi być wiekszy od 0!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    returnValue = false;
                } else {
                    optionsDAO.saveRefreshInterval(Double.valueOf((String) newValue) * 60);
                    ContentResolver.addPeriodicSync(WeatherAlert.CreateSyncAccount(getActivity()), AUTHORITY, Bundle.EMPTY, Long.valueOf((String) newValue) * 60);
                }
                return returnValue;
            }
        });
        getPreferenceScreen().findPreference("pref_show_empty").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                optionsDAO.saveShowEmptyThreats((boolean) newValue);
                return true;
            }
        });
    }
}
