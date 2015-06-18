package pl.pnoga.weatheralert.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pl.pnoga.weatheralert.app.R;
import pl.pnoga.weatheralert.app.model.Threat;
import pl.pnoga.weatheralert.app.utils.Constants;

import java.util.List;

public class ThreatsAdapter extends ArrayAdapter<Threat> {
    public ThreatsAdapter(Context context, List<Threat> threats) {
        super(context, 0, threats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Threat threat = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_threat, parent, false);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.it_threat_icon);
        TextView message = (TextView) convertView.findViewById(R.id.it_threat_message);
        switch (threat.getCode()) {
            case Constants.CODE_RED:
                icon.setImageResource(R.mipmap.red);
                break;
            case Constants.CODE_YELLOW:
                icon.setImageResource(R.mipmap.yellow);
                break;
            case Constants.CODE_GREEN:
                icon.setImageResource(R.mipmap.green);
                break;
        }
        message.setText(threat.getTime() + "\n" + threat.getMessage() + "\n" + threat.getStation().getName());
        return convertView;
    }
}
