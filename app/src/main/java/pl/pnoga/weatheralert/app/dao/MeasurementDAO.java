package pl.pnoga.weatheralert.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import pl.pnoga.weatheralert.app.model.WeatherData;
import pl.pnoga.weatheralert.app.model.WeatherMeasurement;
import pl.pnoga.weatheralert.app.model.WeatherMeasurementList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeasurementDAO extends TableDAO {
    private final String TAG = "MeasurementDAO";
    private final String TABLE_NAME = "measurements";

    public MeasurementDAO(Context context) {
        super(context);
    }

    public void saveMeasurements(List<WeatherMeasurement> weatherMeasurements) {
        int counter = 0;
        for (WeatherMeasurement weatherMeasurement : weatherMeasurements) {
            ContentValues values = new ContentValues();
            values.put("station", weatherMeasurement.getStation());
            values.put("time", weatherMeasurement.getTime());
            values.put("pressure", weatherMeasurement.getData().getPressure());
            values.put("temperature", weatherMeasurement.getData().getTemperature());
            values.put("dewPointTemperature", weatherMeasurement.getData().getDewPointTemperature());
            values.put("moisture", weatherMeasurement.getData().getMoisture());
            values.put("lastHourDrop", weatherMeasurement.getData().getLastHourDrop());
            values.put("showers", weatherMeasurement.getData().getShowers());
            values.put("windDirection", weatherMeasurement.getData().getWindDirection());
            values.put("windSpeed", weatherMeasurement.getData().getWindSpeed());
            values.put("momentaryWindSpeed", weatherMeasurement.getData().getMomentaryWindSpeed());
            if (database.insert(TABLE_NAME, null, values) == -1)
                Log.d(TAG, "Failed insert of measurements: " + weatherMeasurement.getStation() + " @ " + weatherMeasurement.getTime());
            else ++counter;
        }
        Log.d(TAG, "Saved " + counter + " measurements");
    }

    public WeatherMeasurementList getMeasurmentsAfterDateForStation(Date date, String stationName) {
        WeatherMeasurementList weatherMeasurements = new WeatherMeasurementList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"station", "time", "pressure", "temperature", "dewPointTemperature", "moisture", "lastHourDrop", "showers", "windDirection", "windSpeed", "momentaryWindSpeed"}, " time > \"" + simpleDateFormat.format(date) + "\" AND station =\"" + stationName + "\"", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WeatherMeasurement weatherMeasurement = new WeatherMeasurement();
            weatherMeasurement.setStation(cursor.getString(0));
            weatherMeasurement.setTime(cursor.getString(1));
            weatherMeasurement.setData(new WeatherData());
            weatherMeasurement.getData().setPressure(cursor.getDouble(2));
            weatherMeasurement.getData().setTemperature(cursor.getDouble(3));
            weatherMeasurement.getData().setDewPointTemperature(cursor.getDouble(4));
            weatherMeasurement.getData().setMoisture(cursor.getDouble(5));
            weatherMeasurement.getData().setLastHourDrop(cursor.getDouble(6));
            weatherMeasurement.getData().setShowers(cursor.getDouble(7));
            weatherMeasurement.getData().setWindDirection(cursor.getDouble(8));
            weatherMeasurement.getData().setWindSpeed(cursor.getDouble(9));
            weatherMeasurement.getData().setMomentaryWindSpeed(cursor.getDouble(10));
            weatherMeasurements.add(weatherMeasurement);
            cursor.moveToNext();
        }
        cursor.close();
        return weatherMeasurements;
    }

    public WeatherMeasurement getMeasurmentsForStation(String stationName) {
        WeatherMeasurement weatherMeasurement = null;
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"station", "time", "pressure", "temperature", "dewPointTemperature", "moisture", "lastHourDrop", "showers", "windDirection", "windSpeed", "momentaryWindSpeed"}, " station =\"" + stationName + "\"", null, null, null, " time DESC", " 1");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            weatherMeasurement = new WeatherMeasurement();
            weatherMeasurement.setStation(cursor.getString(0));
            weatherMeasurement.setTime(cursor.getString(1));
            weatherMeasurement.setData(new WeatherData());
            weatherMeasurement.getData().setPressure(cursor.getDouble(2));
            weatherMeasurement.getData().setTemperature(cursor.getDouble(3));
            weatherMeasurement.getData().setDewPointTemperature(cursor.getDouble(4));
            weatherMeasurement.getData().setMoisture(cursor.getDouble(5));
            weatherMeasurement.getData().setLastHourDrop(cursor.getDouble(6));
            weatherMeasurement.getData().setShowers(cursor.getDouble(7));
            weatherMeasurement.getData().setWindDirection(cursor.getDouble(8));
            weatherMeasurement.getData().setWindSpeed(cursor.getDouble(9));
            weatherMeasurement.getData().setMomentaryWindSpeed(cursor.getDouble(10));
            cursor.moveToNext();
        }
        cursor.close();
        return weatherMeasurement;
    }
}
