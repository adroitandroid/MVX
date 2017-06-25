package com.adroitandroid.weatherapp;

import com.adroitandroid.mvx.XPresenter;

/**
 * Created by pv on 25/06/17.
 */

public class MainPresenter extends XPresenter<MainView, MainPresenterModel> {

    protected static final String INFO_MESSAGE = "Please enter your name to display here";
    protected static final String ERROR_MESSAGE = "Please enter a valid name";

    void submitInput(String input) {
        if (input == null || input.length() == 0) {
            getView().setError(ERROR_MESSAGE);
        } else {
            getPresenterModel().setName(input);
            getView().setText(input);
        }
    }

    String getText() {
        String name = getPresenterModel().getName();
        if (name == null) {
            return INFO_MESSAGE;
        } else {
            return name;
        }
    }
}
