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
            "time datetime not null, " +
            "pressure double," +
            "temperature double," +
            "dewPointTemperature double," +
            "moisture double," +
            "lastHourDrop double," +
            "showers double," +
            "windDirection double," +
            "windSpeed double," +
            "momentaryWindSpeed double, " +
            "UNIQUE(station, time) ON CONFLICT IGNORE);";
    private final String CREATE_THREATS_TABLE = "CREATE TABLE threats(" +
            "code int," +
            "message text," +
            "time datetime," +
            "station text," +
            "is_empty integer," +
            "FOREIGN KEY(station) REFERENCES stations(station));";
    private final String CREATE_OPTIONS_TABLE = "CREATE TABLE options(" +
            "name text," +
            "value double," +
            "UNIQUE (name) ON CONFLICT IGNORE)";
    private final String INSERT_DEFAULT_OPTIONS = "INSERT INTO options(name, value) VALUES (\"CRIT_MAX_TEMPERATURE\", \"30.0\"), " +
            "(\"CRIT_MIN_TEMPERATURE\", \"-15.0\"), " +
            "(\"CRIT_WIND_SPEED\", \"20.0\"), " +
            "(\"CRIT_SHOWER\", \"30.0\"), " +
            "(\"MAX_RADIUS\", \"20.0\"), " +
            "(\"MAX_CLOSE_RADIUS\", \"10.0\"), " +
            "(\"REFRESH_INTERVAL\", \"1800\"), " +
            "(\"SHOW_EMPTY_THREATS\", \"1\");";

    private final String DROP_STATIONS = "DROP TABLE IF EXISTS stations;";
    private final String DROP_MEASURMENTS = "DROP TABLE IF EXISTS measurements;";
    private final String DROP_THREATS = "DROP TABLE IF EXISTS threats;";
    private final String DROP_OPTIONS = "DROP TABLE IF EXIST options";
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATIONS_TABLE);
        db.execSQL(CREATE_MEASURMENTS_TABLE);
        db.execSQL(CREATE_THREATS_TABLE);
        db.execSQL(CREATE_OPTIONS_TABLE);
        db.execSQL(INSERT_DEFAULT_OPTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STATIONS);
        db.execSQL(DROP_MEASURMENTS);
        db.execSQL(DROP_THREATS);
        db.execSQL(DROP_OPTIONS);
        onCreate(db);
    }

}
