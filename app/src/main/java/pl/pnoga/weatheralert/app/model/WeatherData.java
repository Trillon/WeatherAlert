package pl.pnoga.weatheralert.app.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {
    @JsonProperty("p0")
    private double pressure;
    @JsonProperty("ta")
    private double temperature;
    @JsonProperty("t0")
    private double dewPointTemperature;
    @JsonProperty("ha")
    private double moisture;
    @JsonProperty("r1")
    private double lastHourDrop;
    @JsonProperty("ra")
    private double showers;
    @JsonProperty("wd")
    private double windDirection;
    @JsonProperty("ws")
    private double windSpeed;
    @JsonProperty("wg")
    private double momentaryWindSpeed;

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getDewPointTemperature() {
        return dewPointTemperature;
    }

    public void setDewPointTemperature(double dewPointTemperature) {
        this.dewPointTemperature = dewPointTemperature;
    }

    public double getMoisture() {
        return moisture;
    }

    public void setMoisture(double moisture) {
        this.moisture = moisture;
    }

    public double getLastHourDrop() {
        return lastHourDrop;
    }

    public void setLastHourDrop(double lastHourDrop) {
        this.lastHourDrop = lastHourDrop;
    }

    public double getShowers() {
        return showers;
    }

    public void setShowers(double showers) {
        this.showers = showers;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getMomentaryWindSpeed() {
        return momentaryWindSpeed;
    }

    public void setMomentaryWindSpeed(double momentaryWindSpeed) {
        this.momentaryWindSpeed = momentaryWindSpeed;
    }
}
