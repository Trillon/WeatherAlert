package pl.pnoga.weatheralert.app.dao;

import android.content.Context;

public class OptionsDAO extends TableDAO {
    private final String TAG = "OptionsDAO";
    private final String TABLE_NAME = "options";

    public OptionsDAO(Context context) {
        super(context);
    }
}
