package pl.pnoga.weatheralert.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather_alert_database";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_STATIONS_TABLE = "CREATE TABLE stations(" +
            "name text not null," +
            "station text not null," +
            "lati double not null," +
            "long double not null," +
            "alti double not null, " +
            "UNIQUE(station) ON CONFLICT IGNORE);";
    private final String CREATE_MEASURMENTS_TABLE = "CREATE TABLE measurements(" +
            "station text not null," +
            "time date not null, " +
            "pressure double," +
            "temperature double," +
            "dewPointTemperature double," +
            "moisture double," +
            "lastHourDrop double," +
            "showers double," +
            "windDirection double," +
            "windSpeed double," +
            "momentaryWindSpeed double, " +
            "UNIQUE(station, time) ON CONFLICT REPLACE);";

    private final String DROP_STATIONS = "DROP TABLE IF EXISTS stations;";
    private final String DROP_MEASURMENTS = "DROP TABLE IF EXISTS measurements;";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATIONS_TABLE);
        db.execSQL(CREATE_MEASURMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STATIONS);
        db.execSQL(DROP_MEASURMENTS);
        onCreate(db);
    }

}
