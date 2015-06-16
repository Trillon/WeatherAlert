package pl.pnoga.weatheralert.app.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import pl.pnoga.weatheralert.app.DatabaseManager;

public class TableDAO {
    protected SQLiteDatabase database;
    protected DatabaseManager dbManager;

    public TableDAO(Context context) {
        dbManager = new DatabaseManager(context);
    }

    public void open() throws SQLException {
        database = dbManager.getWritableDatabase();
    }

    public void close() {
        dbManager.close();
    }
}
