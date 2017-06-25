package com.adroitandroid.weatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pv on 25/06/17.
 */

public class WeatherData {

    @SerializedName("weather")
    @Expose
    private List<Weather> weather = new ArrayList<>();

    @SerializedName("main")
    @Expose
    private WeatherDataMain main;

    @SerializedName("name")
    @Expose
    private String name;

    public List<Weather> getWeather() {
        return weather;
    }

    public Weather getWeatherSummary() {
        return weather == null || weather.size() == 0 ? null : weather.get(0);
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public WeatherDataMain getMain() {
        return main;
    }

    public void setMain(WeatherDataMain main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
