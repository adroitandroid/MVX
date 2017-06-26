package com.adroitandroid.weatherapp.network;

import com.adroitandroid.weatherapp.model.WeatherData;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pv on 25/06/17.
 */

public interface WeatherApiClient {

    @GET("weather?appid=04cf27cc0891a74dab4e5e742a41f16d")
    Observable<WeatherData> getWeatherData(@Query("q") String zipCodeAndCountry);
}
