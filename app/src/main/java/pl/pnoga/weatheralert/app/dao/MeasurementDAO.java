package pl.pnoga.weatheralert.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.util.Log;
import pl.pnoga.weatheralert.app.model.WeatherMeasurement;

import java.util.List;

public class MeasurementDAO extends TableDAO {
    private final String TAG = "MeasurementDAO";
    private final String TABLE_NAME = "measurements";

    public MeasurementDAO(Context context) {
        super(context);
    }

    public void saveMeasurements(List<WeatherMeasurement> weatherMeasurements) {
        int counter = 0;
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public long getMeasurementsCount() {
        return DatabaseUtils.longForQuery(database, "SELECT COUNT(*) FROM " + TABLE_NAME, null);
    }
}
