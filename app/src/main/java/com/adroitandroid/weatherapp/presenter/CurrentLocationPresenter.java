package com.adroitandroid.weatherapp.presenter;

import com.adroitandroid.mvx.lce.XLcePresenter;
import com.adroitandroid.mvx.lce.XLceView;
import com.adroitandroid.weatherapp.model.CurrentLocationPresenterModel;
import com.adroitandroid.weatherapp.model.IpLocationData;
import com.adroitandroid.weatherapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pv on 26/06/17.
 */

public class CurrentLocationPresenter extends XLcePresenter<IpLocationData, XLceView<IpLocationData>, CurrentLocationPresenterModel> {
    @Override
    protected void onFetchComplete(IpLocationData data) {
        String countryCode = data.getCountryCode();
        Locale locale = new Locale("", countryCode);
        String displayCountry = locale.getDisplayCountry();
        getPresenterModel().setCurrentCountryIndex(getPresenterModel().getCountriesList().indexOf(displayCountry));
    }

    @Override
    protected void onFetchError(String error) {

    }

    @Override
    protected void onStartFetch() {
        RetrofitClient.getIpApiClient().getIpLocationData().enqueue(new Callback<IpLocationData>() {
            @Override
            public void onResponse(Call<IpLocationData> call, Response<IpLocationData> response) {
                complete(response.body());
                getPresenterModel().setStatus(CurrentLocationPresenterModel.STATUS_FETCH_COMPLETE);
            }

            @Override
            public void onFailure(Call<IpLocationData> call, Throwable t) {
                setError("Couldn't find your current location");
            }
        });
    }

    public List<String> getCountriesList() {
        List<String> countriesList = getPresenterModel().getCountriesList();
        if (countriesList == null) {
            countriesList = new ArrayList<>();
            String[] isoCountries = Locale.getISOCountries();
            for (String iso : isoCountries) {
                Locale locale = new Locale("", iso);
                String displayCountry = locale.getDisplayCountry();
                if (displayCountry.trim().length() > 0 && !countriesList.contains(displayCountry)) {
                    countriesList.add(displayCountry);
                }
            }
            Collections.sort(countriesList);
            getPresenterModel().setCountriesList(countriesList);
        }
        return countriesList;
    }

    public int getCurrentCountryIndex() {
        return getPresenterModel().getCurrentCountryIndex();
    }

    public boolean isLocationFetched() {
        return getPresenterModel().getStatus() == CurrentLocationPresenterModel.STATUS_FETCH_COMPLETE;
    }

    public void setSelectedCountryIndex(int position) {
        getPresenterModel().setSelectedCountryIndex(position);
    }

    public int getSelectedCountryIndex() {
        return getPresenterModel().getSelectedCountryIndex();
    }
}
