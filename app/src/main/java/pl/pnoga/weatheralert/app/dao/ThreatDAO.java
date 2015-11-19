package pl.pnoga.weatheralert.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import pl.pnoga.weatheralert.app.model.Threat;
import pl.pnoga.weatheralert.app.model.ThreatList;

public class ThreatDAO extends TableDAO {
    private final String TAG = "ThreatDAO";
    private final String TABLE_NAME = "threats";

    public ThreatDAO(Context context) {
        super(context);
    }

    public void saveThreats(ThreatList threats) {
        deleteAll();
        int counter = 0;
        for (Threat threat : threats) {
            ContentValues values = new ContentValues();
            values.put("code", threat.getCode());
            values.put("message", threat.getMessage());
            values.put("time", threat.getTime());
            values.put("station", threat.getStation().getStation());
            values.put("is_empty", threat.getIsEmpty());
            if (database.insert(TABLE_NAME, null, values) == -1)
                Log.d(TAG, "Failed insert of threat: " + threat.getTime());
            else ++counter;
        }
        Log.d(TAG, "Saved " + counter + " threats");
    }

    public ThreatList getAllThreats() {
        Log.d(TAG, "getAllThreats");
        ThreatList threats = new ThreatList();
        StationDAO stationDAO = new StationDAO(context);
        stationDAO.open();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"code", "message", "time", "station", "is_empty"}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Threat threat = new Threat();
            threat.setCode(cursor.getInt(0));
            threat.setMessage(cursor.getString(1));
            threat.setTime(cursor.getString(2));
            threat.setStation(stationDAO.getStationById(cursor.getString(3)));
            threat.setIsEmpty(cursor.getInt(4));
            threats.add(threat);
            cursor.moveToNext();
        }
        cursor.close();
        stationDAO.close();
        return threats;
    }

    public ThreatList getNonEmptyThreats() {
        Log.d(TAG, "getNonEmptyThreats");
        ThreatList threats = new ThreatList();
        StationDAO stationDAO = new StationDAO(context);
        stationDAO.open();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"code", "message", "time", "station", "is_empty"}, "is_empty=0", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Threat threat = new Threat();
            threat.setCode(cursor.getInt(0));
            threat.setMessage(cursor.getString(1));
            threat.setTime(cursor.getString(2));
            threat.setStation(stationDAO.getStationById(cursor.getString(3)));
            threat.setIsEmpty(cursor.getInt(4));
            threats.add(threat);
            cursor.moveToNext();
        }
        cursor.close();
        stationDAO.close();
        return threats;
    }

    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }
}
