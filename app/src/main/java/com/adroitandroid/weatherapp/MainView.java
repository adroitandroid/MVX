package com.adroitandroid.weatherapp;

import com.adroitandroid.mvx.XView;

/**
 * Created by pv on 25/06/17.
 */

interface MainView extends XView {
    void setError(String error);

    void setText(String input);
}
