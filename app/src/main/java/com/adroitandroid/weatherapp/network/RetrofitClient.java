package com.adroitandroid.weatherapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pv on 25/06/17.
 */

public class RetrofitClient {

    private static Retrofit weather = null;
    private static Retrofit ip = null;
    private static final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String IP_BASE_URL = "http://ip-api.com/";

    public static WeatherApiClient getWeatherApiClient() {
        return RetrofitClient.getWeatherClient().create(WeatherApiClient.class);
    }

    private static Retrofit getWeatherClient() {
        if (weather == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            weather = new Retrofit.Builder()
                    .baseUrl(WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return weather;
    }

    public static IpApiClient getIpApiClient() {
        return RetrofitClient.getIpClient().create(IpApiClient.class);
    }

    private static Retrofit getIpClient() {
        if (ip == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            ip = new Retrofit.Builder()
                    .baseUrl(IP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return ip;
    }
}
