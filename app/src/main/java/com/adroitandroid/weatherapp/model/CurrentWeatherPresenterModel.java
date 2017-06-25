package com.adroitandroid.weatherapp.model;

import com.adroitandroid.mvx.lce.XLcePresenterModel;
import com.adroitandroid.weatherapp.presenter.CurrentWeatherPresenter;
import com.adroitandroid.weatherapp.view.CurrentWeatherView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pv on 25/06/17.
 */

public class CurrentWeatherPresenterModel extends XLcePresenterModel<WeatherData, CurrentWeatherView> {
    public static final int STATE_FETCH_IN_PROGRESS = 1;
    public static final int STATE_FETCH_COMPLETE = 2;
    public static final int STATE_FETCH_FAILED = 3;
    private static final long CACHE_EXPIRY_IN_MINUTES = 15;
    private WeatherData mLastFetchedData;
    private String mZipCode;
    private String mCountryCode;
    private int mState;
    private HashMap<String, WeatherData> mWeatherDataCache = new HashMap<>();

    @Override
    public CurrentWeatherPresenter getPresenter() {
        return new CurrentWeatherPresenter();
    }

    public void setLastFetchedData(WeatherData data) {
        mLastFetchedData = data;
        final String zipCodeAndCountryCode = getZipCodeAndCountryCode();
        if (getPresenter().mustCache(mZipCode, mCountryCode)
                && mWeatherDataCache.get(zipCodeAndCountryCode) == null) {
            mWeatherDataCache.put(zipCodeAndCountryCode, data);
            Observable.timer(CACHE_EXPIRY_IN_MINUTES, TimeUnit.MINUTES)
                    .observeOn(Schedulers.computation())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mWeatherDataCache.remove(zipCodeAndCountryCode);
                        }
                    });
        }
    }

    public WeatherData getLastFetchedData() {
        return mLastFetchedData;
    }

    public void setZipCodeForNewFetch(String zipCode) {
        mZipCode = zipCode;
    }

    public void setCountryCodeForNewFetch(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getZipCodeAndCountryCode() {
        return mZipCode + "," + mCountryCode;
    }

    public WeatherData getWeatherDataFromCache() {
        return mWeatherDataCache.get(getZipCodeAndCountryCode());
    }

    public void setStatus(int status) {
        mState = status;
    }

    public int getStatus() {
        return mState;
    }

    public String getZipCodeForNewFetch() {
        return mZipCode;
    }

    public String getCountryCodeForNewFetch() {
        return mCountryCode;
    }
}
