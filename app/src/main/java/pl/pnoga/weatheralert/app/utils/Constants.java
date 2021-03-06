package pl.pnoga.weatheralert.app.utils;

public class Constants {
    public static final int CODE_RED = 0;
    public static final int CODE_YELLOW = 1;
    public static final int CODE_GREEN = 2;
    public static final double CRIT_MAX_TEMPERATURE_DEAFAULT_VALUE = 30.0;
    public static final double CRIT_MIN_TEMPERATURE_DEAFAULT_VALUE = -15.0;
    public static final double CRIT_WIND_SPEED_DEAFAULT_VALUE = 20.0;
    public static final double CRIT_SHOWER_DEAFAULT_VALUE = 30.0;
    public static final double MAX_RADIUS_DEAFAULT_VALUE = 20.0;
    public static final double MAX_CLOSE_RADIUS_DEAFAULT_VALUE = 10.0;
    public static final int REFRESH_INTERVAL = 30 * 60;
    public static final String AUTHORITY = "pl.pnoga.weatheralert.provider";
    public static final String ACCOUNT_TYPE = "pnoga.pl";
    public static final String ACCOUNT = "WeatherAlert";
    public static final String ACTION_FINISHED_SYNC = "pl.pnoga.weatheralert.app.activity.ACTION_FINISHED_SYNC";
    public static final int EMPTY_THREAT = 1;
    public static final int NON_EMPTY_THREAT = 0;
    public static final int SHOW_EMPTY_THREAT = 1;
}
