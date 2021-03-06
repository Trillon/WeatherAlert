package pl.pnoga.weatheralert.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import pl.pnoga.weatheralert.app.utils.Constants;

public class OptionsDAO extends TableDAO {
    private final String TAG = "OptionsDAO";
    private final String TABLE_NAME = "options";
    private final String CRIT_MAX_TEMPERATURE = "CRIT_MAX_TEMPERATURE";
    private final String CRIT_MIN_TEMPERATURE = "CRIT_MIN_TEMPERATURE";
    private final String CRIT_WIND_SPEED = "CRIT_WIND_SPEED";
    private final String CRIT_SHOWER = "CRIT_SHOWER";
    private final String MAX_RADIUS = "MAX_RADIUS";
    private final String MAX_CLOSE_RADIUS = "MAX_CLOSE_RADIUS";
    private final String REFRESH_INTERVAL = "REFRESH_INTERVAL";
    private final String SHOW_EMPTY_THREATS = "SHOW_EMPTY_THREATS";


    public OptionsDAO(Context context) {
        super(context);
    }

    public double getMaxCritTemperature() {
        double maxCritTemperature = Constants.CRIT_MAX_TEMPERATURE_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + CRIT_MAX_TEMPERATURE+"\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            maxCritTemperature = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_MAX_TEMPERATURE + " " + maxCritTemperature);
        return maxCritTemperature;
    }

    public double getMinCritTemperature() {
        double minCritTemperature = Constants.CRIT_MIN_TEMPERATURE_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + CRIT_MIN_TEMPERATURE+"\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            minCritTemperature = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_MIN_TEMPERATURE + " " + minCritTemperature);
        return minCritTemperature;
    }

    public double getCritWindSpeed() {
        double windSpeed = Constants.CRIT_WIND_SPEED_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + CRIT_WIND_SPEED+"\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            windSpeed = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_WIND_SPEED + " " + windSpeed);
        return windSpeed;
    }

    public double getCritShower() {
        double shower = Constants.CRIT_SHOWER_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + CRIT_SHOWER+"\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            shower = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_SHOWER + " " + shower);
        return shower;
    }

    public double getMaxRadius() {
        double maxRadius = Constants.MAX_RADIUS_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + MAX_RADIUS+"\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            maxRadius = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, MAX_RADIUS + " " + maxRadius);
        return maxRadius;
    }

    public double getCloseRadius() {
        double closeRadius = Constants.MAX_CLOSE_RADIUS_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + MAX_CLOSE_RADIUS+"\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            closeRadius = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, MAX_CLOSE_RADIUS + " " + closeRadius);
        return closeRadius;
    }

    public double getRefreshInterval() {
        double refreshInterval = Constants.REFRESH_INTERVAL;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + REFRESH_INTERVAL + "\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            refreshInterval = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, REFRESH_INTERVAL + " " + refreshInterval);
        return refreshInterval;
    }

    public double getShowEmptyThreats() {
        double refreshInterval = Constants.SHOW_EMPTY_THREAT;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = \"" + SHOW_EMPTY_THREATS + "\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            refreshInterval = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, SHOW_EMPTY_THREATS + " " + refreshInterval);
        return refreshInterval;
    }

    public void saveMaxCritTemperature(double maxTemperatureValue) {
        ContentValues values = new ContentValues();
        values.put("value", maxTemperatureValue);
        if (database.update(TABLE_NAME, values, "name = \"" + CRIT_MAX_TEMPERATURE + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + CRIT_MAX_TEMPERATURE);
        else Log.d(TAG, "Saved " + CRIT_MAX_TEMPERATURE);
    }

    public void saveMinCritTemperature(double minTemperatureValue) {
        ContentValues values = new ContentValues();
        values.put("value", minTemperatureValue);
        if (database.update(TABLE_NAME, values, "name = \"" + CRIT_MIN_TEMPERATURE + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + CRIT_MIN_TEMPERATURE);
        else Log.d(TAG, "Saved " + CRIT_MIN_TEMPERATURE);
    }

    public void saveCritWindSpeed(double windSpeed) {
        ContentValues values = new ContentValues();
        values.put("value", windSpeed);
        if (database.update(TABLE_NAME, values, "name = \"" + CRIT_WIND_SPEED + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + CRIT_WIND_SPEED);
        else Log.d(TAG, "Saved " + CRIT_WIND_SPEED);
    }

    public void saveCritShower(double shower) {
        ContentValues values = new ContentValues();
        values.put("value", shower);
        if (database.update(TABLE_NAME, values, "name = \"" + CRIT_SHOWER + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + CRIT_SHOWER);
        else Log.d(TAG, "Saved " + CRIT_SHOWER);
    }

    public void saveMaxRadius(double maxRadius) {
        ContentValues values = new ContentValues();
        values.put("value", maxRadius);
        if (database.update(TABLE_NAME, values, "name = \"" + MAX_RADIUS + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + MAX_RADIUS);
        else Log.d(TAG, "Saved " + MAX_RADIUS);
    }

    public void saveCloseRadius(double closeRadius) {
        ContentValues values = new ContentValues();
        values.put("value", closeRadius);
        if (database.update(TABLE_NAME, values, "name = \"" + MAX_CLOSE_RADIUS + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + MAX_CLOSE_RADIUS);
        else Log.d(TAG, "Saved " + MAX_CLOSE_RADIUS);
    }

    public void saveRefreshInterval(double refreshInterval) {
        ContentValues values = new ContentValues();
        values.put("value", refreshInterval);
        if (database.update(TABLE_NAME, values, "name = \"" + REFRESH_INTERVAL + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + REFRESH_INTERVAL);
        else Log.d(TAG, "Saved " + REFRESH_INTERVAL);
    }

    public void saveShowEmptyThreats(boolean showEmptyThreats) {
        ContentValues values = new ContentValues();
        values.put("value", showEmptyThreats ? 1.0 : 0.0);
        if (database.update(TABLE_NAME, values, "name = \"" + SHOW_EMPTY_THREATS + "\"", null) == 0)
            Log.d(TAG, "Failed insert of value " + SHOW_EMPTY_THREATS);
        else Log.d(TAG, "Saved " + SHOW_EMPTY_THREATS);
    }
}
