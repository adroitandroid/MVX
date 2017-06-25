package com.adroitandroid.weatherapp;

import com.adroitandroid.mvx.XPresenter;

/**
 * Created by pv on 25/06/17.
 */

class MainPresenter extends XPresenter<MainView, MainPresenterModel> {

    void submitInput(String input) {
        if (input == null || input.length() == 0) {
            getView().setError("Please enter a valid name");
        } else {
            getPresenterModel().setName(input);
            getView().setText(input);
        }
    }

    String getText() {
        String name = getPresenterModel().getName();
        if (name == null) {
            return "Please enter your name to display here";
        } else {
            return name;
        }
    }
}
