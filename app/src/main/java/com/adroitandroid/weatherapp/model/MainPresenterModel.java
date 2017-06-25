package com.adroitandroid.weatherapp.model;

import com.adroitandroid.mvx.XPresenterModel;
import com.adroitandroid.weatherapp.presenter.MainPresenter;
import com.adroitandroid.weatherapp.view.MainView;

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
