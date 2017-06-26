package com.adroitandroid.weatherapp.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.adroitandroid.mvx.XPresenter;
import com.adroitandroid.weatherapp.R;
import com.adroitandroid.weatherapp.databinding.ActivityCurrentWeatherBinding;
import com.adroitandroid.weatherapp.model.CurrentLocationPresenterModel;
import com.adroitandroid.weatherapp.model.CurrentWeatherPresenterModel;
import com.adroitandroid.weatherapp.model.IpLocationData;
import com.adroitandroid.weatherapp.model.WeatherData;
import com.adroitandroid.weatherapp.presenter.CurrentLocationPresenter;
import com.adroitandroid.weatherapp.presenter.CurrentWeatherPresenter;

/**
 * Created by pv on 25/06/17.
 */

public class CurrentWeatherActivity extends AppCompatActivity {

    private ActivityCurrentWeatherBinding mDataBinding;
    private CurrentWeatherPresenter mWeatherPresenter;
    private CurrentLocationPresenter mLocationPresenter;
    private CurrentWeatherView mCurrentWeatherView;
    private CurrentLocationView mCurrentLocationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_current_weather);
        setTitle("Weather Today");

        initWeatherView();

        initLocationView();

        XPresenter.bind(mCurrentWeatherView, CurrentWeatherPresenterModel.class,
                new XPresenter.OnBindListener<CurrentWeatherPresenter>() {
                    @Override
                    public void onBind(CurrentWeatherPresenter presenter) {
                        mWeatherPresenter = presenter;

                        if (mWeatherPresenter.isFetchInProgress()) {
                            mWeatherPresenter.startFetch();
                        } else {
                            WeatherData weatherData = mWeatherPresenter.getLastWeatherFetch();
                            if (weatherData != null) {
                                mCurrentWeatherView.onContentReady(weatherData);
                            } else {
                                mDataBinding.currentWeatherCv.setVisibility(View.GONE);
                                mDataBinding.progressBarLl.setVisibility(View.GONE);
                            }
                        }
                    }
                });

        XPresenter.bind(mCurrentLocationView, CurrentLocationPresenterModel.class,
                new XPresenter.OnBindListener<CurrentLocationPresenter>() {
                    @Override
                    public void onBind(CurrentLocationPresenter presenter) {
                        mLocationPresenter = presenter;
                        mDataBinding.countriesSpinner.setAdapter(new ArrayAdapter<>(
                                CurrentWeatherActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                presenter.getCountriesList()));

                        mDataBinding.countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mLocationPresenter.setSelectedCountryIndex(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        mDataBinding.countriesSpinner.setSelection(mLocationPresenter.getSelectedCountryIndex());

                        if (!presenter.isLocationFetched()) {
                            presenter.startFetch();
                        }
                    }
        });

        mDataBinding.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeatherPresenter.setFetchParams(mDataBinding.zipcodeEt.getText().toString(),
                        mDataBinding.countriesSpinner.getSelectedItem().toString());
                mWeatherPresenter.startFetch();
            }
        });
    }

    private void initLocationView() {
        mCurrentLocationView = new CurrentLocationView() {

            Snackbar snackbar;

            @Override
            public void onContentReady(IpLocationData content) {
                snackbar.dismiss();
                if (mDataBinding.countriesSpinner.getSelectedItemPosition() == 0) {
                    mDataBinding.countriesSpinner.setSelection(mLocationPresenter.getCurrentCountryIndex());
                }
            }

            @Override
            public void onError(String error) {
                snackbar.dismiss();
                Snackbar.make(mDataBinding.getRoot(), "Couldn't fetch your current location", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFetchStart() {
                snackbar = Snackbar.make(mDataBinding.getRoot(), "Attempting to fetch current location",
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }

            @Override
            public Context getContext() {
                return CurrentWeatherActivity.this;
            }
        };
    }

    private void initWeatherView() {
        mCurrentWeatherView = new CurrentWeatherView() {
            @Override
            public void onContentReady(WeatherData content) {
                mDataBinding.setWeatherData(content);

                mDataBinding.changeLocationLl.setVisibility(View.VISIBLE);
                mDataBinding.currentWeatherCv.setVisibility(View.VISIBLE);
                mDataBinding.progressBarLl.setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {
                Snackbar.make(mDataBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFetchStart() {
                mDataBinding.changeLocationLl.setVisibility(View.GONE);
                mDataBinding.currentWeatherCv.setVisibility(View.GONE);
                mDataBinding.progressTv.setText(mWeatherPresenter.getLoadingText());
                mDataBinding.progressBarLl.setVisibility(View.VISIBLE);
            }

            @Override
            public Context getContext() {
                return CurrentWeatherActivity.this;
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeatherPresenter.unbind(this);
        mLocationPresenter.unbind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWeatherPresenter.saveState();
        mLocationPresenter.saveState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        TODO: move presenters to disposable state
    }

    @Override
    public void finish() {
        super.finish();
//        TODO: move presenters to disposable state
    }
}
