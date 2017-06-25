package com.adroitandroid.weatherapp.network;

import com.adroitandroid.weatherapp.model.IpLocationData;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pv on 25/06/17.
 */

public interface IpApiClient {

    @GET("json")
    Call<IpLocationData> getIpLocationData();
}
