package com.adroitandroid.weatherapp.presenter;

import com.adroitandroid.mvx.lce.XLcePresenter;
import com.adroitandroid.mvx.lce.XLceView;
import com.adroitandroid.weatherapp.model.CurrentWeatherPresenterModel;
import com.adroitandroid.weatherapp.model.WeatherData;
import com.adroitandroid.weatherapp.network.RetrofitClient;

import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pv on 25/06/17.
 */

public class CurrentWeatherPresenter extends XLcePresenter<WeatherData, XLceView<WeatherData>, CurrentWeatherPresenterModel> {
    private static final String MESSAGE_FETCH_IN_PROGRESS = "Fetching weather at the requested location... Please wait.";

    @Override
    protected void onFetchComplete(WeatherData data) {
        getPresenterModel().setStatus(CurrentWeatherPresenterModel.STATE_FETCH_COMPLETE);
        getPresenterModel().setLastFetchedData(data);
    }

    @Override
    protected void onFetchError(String error) {
        getPresenterModel().setStatus(CurrentWeatherPresenterModel.STATE_FETCH_FAILED);
    }

    @Override
    protected void onStartFetch() {
        boolean validZipCode = isValidZipCode();
        if (validZipCode && isValidCountryCode()) {
            WeatherData weatherDataFromCache = getPresenterModel().getWeatherDataFromCache();
            if (weatherDataFromCache != null) {
                complete(weatherDataFromCache);
            } else {
                getPresenterModel().setStatus(CurrentWeatherPresenterModel.STATE_FETCH_IN_PROGRESS);
                RetrofitClient.getWeatherApiClient().getWeatherData(
                        getPresenterModel().getZipCodeAndCountryCode())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<WeatherData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(WeatherData weatherData) {
                                complete(weatherData);
                            }

                            @Override
                            public void onError(Throwable e) {
                                setError("Unable to fetch weather for location " + getPresenterModel().getZipCodeAndCountryCode());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        } else {
            if (!validZipCode) {
                setError("Please enter a valid zip code");
            } else {
                setError("Please select a valid country");
            }
        }
    }

    private boolean isValidCountryCode() {
        String countryCodeForNewFetch = getPresenterModel().getCountryCodeForNewFetch();
        return countryCodeForNewFetch != null && countryCodeForNewFetch.length() == 2;
    }

    private boolean isValidZipCode() {
        String zipCodeForNewFetch = getPresenterModel().getZipCodeForNewFetch();
        int zipCodeInt;
        try {
            zipCodeInt = Integer.parseInt(zipCodeForNewFetch);
        } catch (NumberFormatException e) {
            return false;
        }
        return zipCodeForNewFetch != null && zipCodeForNewFetch.length() > 0 && zipCodeInt > 0;
    }

    public String getLoadingText() {
        return MESSAGE_FETCH_IN_PROGRESS;
    }

    public WeatherData getLastWeatherFetch() {
        return getPresenterModel().getLastFetchedData();
    }

    public void setFetchParams(String zipCode, String country) {
        getPresenterModel().setZipCodeForNewFetch(zipCode);
        String[] isoCountries = Locale.getISOCountries();
        String countryCode = null;
        for (String iso : isoCountries) {
            if (new Locale("", iso).getDisplayCountry().equals(country)) {
                countryCode = iso;
                break;
            }
        }
        getPresenterModel().setCountryCodeForNewFetch(countryCode);
    }

    public boolean isFetchInProgress() {
        return getPresenterModel().getStatus() == CurrentWeatherPresenterModel.STATE_FETCH_IN_PROGRESS;
    }

    public boolean mustCache(String zipCode, String countryCode) {
        int postalCodeInt = Integer.parseInt(zipCode);
        return "US".equalsIgnoreCase(countryCode) && postalCodeInt >= 90000 && postalCodeInt <= 90400;
    }
}
