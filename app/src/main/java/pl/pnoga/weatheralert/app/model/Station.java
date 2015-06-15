package pl.pnoga.weatheralert.app.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Station {

    private String name;
    private String station;
    private double lati;
    @JsonProperty("long")
    private double longi;
    private double alti;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getAlti() {
        return alti;
    }

    public void setAlti(double alti) {
        this.alti = alti;
    }
}
