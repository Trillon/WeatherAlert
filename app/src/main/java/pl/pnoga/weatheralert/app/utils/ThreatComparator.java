package pl.pnoga.weatheralert.app.utils;

import pl.pnoga.weatheralert.app.model.Threat;

import java.util.Comparator;

public class ThreatComparator implements Comparator<Threat> {
    @Override
    public int compare(Threat lhs, Threat rhs) {
        return lhs.getCode() < rhs.getCode() ? -1 : lhs.getCode() == rhs.getCode() ? 0 : 1;
    }
}
