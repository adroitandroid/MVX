package com.adroitandroid.weatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pv on 25/06/17.
 */

public class WeatherDataMain {

    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("pressure")
    @Expose
    private Double pressure;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("temp_min")
    @Expose
    private Double tempMin;
    @SerializedName("temp_max")
    @Expose
    private Double tempMax;

    public boolean isTempPresent() {
        return temp != null;
    }

    public boolean isPressurePresent() {
        return pressure != null;
    }

    public boolean isMinTempPresent() {
        return tempMin != null;
    }

    public boolean isMaxTempPresent() {
        return tempMax != null;
    }

    public boolean isHumidityPresent() {
        return humidity != null;
    }

    public double getTempInK() {
        return (temp * 9 / 5) - 459.67;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public double getTempMinInK() {
        return (tempMin * 9 / 5) - 459.67;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMaxInK() {
        return (tempMax * 9 / 5) - 459.67;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }
}
