package pl.pnoga.weatheralert.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.fragment.OptionsFragment;

public class OptionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getFragmentManager().beginTransaction().replace(R.id.options_layout, new OptionsFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info_settings) {
            final Dialog dialog = new Dialog(OptionsActivity.this);
            dialog.setContentView(R.layout.about_dialog);
            dialog.setTitle("O aplikacji");
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
