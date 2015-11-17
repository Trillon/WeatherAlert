package pl.pnoga.weatheralert.app.dao;

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


    public OptionsDAO(Context context) {
        super(context);
    }

    public double getMaxCritTemperature() {
        double maxCritTemperature = Constants.CRIT_MAX_TEMPERATURE_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = " + CRIT_MAX_TEMPERATURE, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            maxCritTemperature = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_MAX_TEMPERATURE + " " + maxCritTemperature);
        return maxCritTemperature;
    }

    public double getMinCritTemperature() {
        double minCritTemperature = Constants.CRIT_MIN_TEMPERATURE_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = " + CRIT_MIN_TEMPERATURE, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            minCritTemperature = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_MIN_TEMPERATURE + " " + minCritTemperature);
        return minCritTemperature;
    }

    public double getCritWindSpeed() {
        double windSpeed = Constants.CRIT_WIND_SPEED_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = " + CRIT_WIND_SPEED, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            windSpeed = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_WIND_SPEED + " " + windSpeed);
        return windSpeed;
    }

    public double getCritShower() {
        double shower = Constants.CRIT_SHOWER_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = " + CRIT_SHOWER, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            shower = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, CRIT_SHOWER + " " + shower);
        return shower;
    }

    public double getMaxRadius() {
        double maxRadius = Constants.MAX_RADIUS_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = " + MAX_RADIUS, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            maxRadius = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, MAX_RADIUS + " " + maxRadius);
        return maxRadius;
    }

    public double getCloseRadius() {
        double closeRadius = Constants.MAX_CLOSE_RADIUS_DEAFAULT_VALUE;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"value"}, "name = " + MAX_CLOSE_RADIUS, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            closeRadius = cursor.getDouble(0);
        }
        cursor.close();
        Log.d(TAG, MAX_CLOSE_RADIUS + " " + closeRadius);
        return closeRadius;
    }
}
