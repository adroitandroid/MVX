package com.adroitandroid.weatherapp.network;

import com.adroitandroid.weatherapp.model.IpLocationData;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by pv on 25/06/17.
 */

public interface IpApiClient {

    @GET("json")
    Observable<IpLocationData> getIpLocationData();
}
