package com.adroitandroid.weatherapp.model;

import com.adroitandroid.mvx.lce.XLcePresenterModel;
import com.adroitandroid.mvx.lce.XLceView;
import com.adroitandroid.weatherapp.presenter.CurrentLocationPresenter;

import java.util.List;

/**
 * Created by pv on 26/06/17.
 */

public class CurrentLocationPresenterModel extends XLcePresenterModel<IpLocationData, XLceView<IpLocationData>> {
    public static final int STATUS_FETCH_COMPLETE = 2;
    private List<String> countriesList;
    private int currentCountryIndex;
    private int status = 0;
    private int selectedCountryIndex;

    public CurrentLocationPresenterModel() {
        super("CurrentLocationPresenterModel");
    }

    @Override
    public CurrentLocationPresenter getPresenter() {
        return new CurrentLocationPresenter();
    }

    public void setCountriesList(List<String> countriesList) {
        this.countriesList = countriesList;
    }

    public List<String> getCountriesList() {
        return countriesList;
    }

    public void setCurrentCountryIndex(int currentCountryIndex) {
        this.currentCountryIndex = currentCountryIndex;
    }

    public int getCurrentCountryIndex() {
        return currentCountryIndex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSelectedCountryIndex(int selectedCountryIndex) {
        this.selectedCountryIndex = selectedCountryIndex;
    }

    public int getSelectedCountryIndex() {
        return selectedCountryIndex;
    }
}
