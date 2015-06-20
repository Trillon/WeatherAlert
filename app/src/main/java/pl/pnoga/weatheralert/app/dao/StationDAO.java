package pl.pnoga.weatheralert.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import pl.pnoga.weatheralert.app.model.Station;
import pl.pnoga.weatheralert.app.model.StationList;

import java.util.ArrayList;
import java.util.List;

public class StationDAO extends TableDAO {
    private final String TAG = "StationDAO";
    private final String TABLE_NAME = "stations";

    public StationDAO(Context context) {
        super(context);
    }

    public void saveStations(List<Station> stationList) {
        int counter = 0;
        for (Station station : stationList) {
            ContentValues values = new ContentValues();
            values.put("name", station.getName());
            values.put("station", station.getStation());
            values.put("lati", station.getLati());
            values.put("long", station.getLongi());
            values.put("alti", station.getAlti());
            if (database.insert(TABLE_NAME, null, values) == -1)
                Log.d(TAG, "Failed insert of station: " + station.getName());
            else ++counter;
        }
        Log.d(TAG, "Saved " + counter + " stations");
    }

    public List<String> getStationsId() {
        List<String> names = new ArrayList<String>();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"station"}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(0);
            names.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }

    public StationList getStations() {
        StationList stations = new StationList();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"name", "station", "lati", "long", "alti"}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Station station = new Station();
            station.setName(cursor.getString(0));
            station.setStation(cursor.getString(1));
            station.setLati(cursor.getDouble(2));
            station.setLongi(cursor.getDouble(3));
            station.setAlti(cursor.getDouble(4));
            stations.add(station);
            cursor.moveToNext();
        }
        cursor.close();
        return stations;
    }

    public Station getStationById(String stationName) {
        Station station = new Station();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"name", "station", "lati", "long", "alti"}, "station = \"" + stationName + "\"", null, null, null, null);
        cursor.moveToFirst();
        station.setName(cursor.getString(0));
        station.setStation(cursor.getString(1));
        station.setLati(cursor.getDouble(2));
        station.setLongi(cursor.getDouble(3));
        station.setAlti(cursor.getDouble(4));
        cursor.moveToNext();
        cursor.close();
        return station;
    }
}
