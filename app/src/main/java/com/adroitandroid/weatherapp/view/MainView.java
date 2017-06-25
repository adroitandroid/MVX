package com.adroitandroid.weatherapp.view;

import com.adroitandroid.mvx.XView;

/**
 * Created by pv on 25/06/17.
 */

public interface MainView extends XView {
    void setError(String error);

    void setText(String input);
}
