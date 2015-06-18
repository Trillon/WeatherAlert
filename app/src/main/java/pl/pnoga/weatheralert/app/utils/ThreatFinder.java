package pl.pnoga.weatheralert.app.utils;

import pl.pnoga.weatheralert.app.model.Threat;

import java.util.ArrayList;
import java.util.List;

public class ThreatFinder {
    public static List<Threat> getThreats() {
        List<Threat> threats = new ArrayList<>();
        Threat threat1 = new Threat();
        threat1.setCode(Constants.CODE_RED);
        threat1.setMessage("Silny wiatr\nStacja AGH");
        Threat threat2 = new Threat();
        threat2.setCode(Constants.CODE_RED);
        threat2.setMessage("Mocne opady\nStacja technikum IV Tarnów");
        Threat threat3 = new Threat();
        threat3.setCode(Constants.CODE_YELLOW);
        threat3.setMessage("Możliwe gradobicie\nStacja Technikum Skawina nr I");
        Threat threat4 = new Threat();
        threat4.setCode(Constants.CODE_GREEN);
        threat4.setMessage("Brak zagrozen\n Stacja Liceum nr II w Krakowie");
        threats.add(threat1);
        threats.add(threat2);
        threats.add(threat3);
        threats.add(threat4);
        return threats;
    }
}
