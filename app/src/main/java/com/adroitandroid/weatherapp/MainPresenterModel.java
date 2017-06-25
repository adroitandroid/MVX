package com.adroitandroid.weatherapp;

import com.adroitandroid.mvx.XPresenterModel;

/**
 * Created by pv on 25/06/17.
 */

public class MainPresenterModel extends XPresenterModel<MainView> {
    private String mName;

    @Override
    public MainPresenter getPresenter() {
        return new MainPresenter();
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
