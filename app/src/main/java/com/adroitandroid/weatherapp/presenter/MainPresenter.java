package com.adroitandroid.weatherapp.presenter;

import com.adroitandroid.mvx.XPresenter;
import com.adroitandroid.weatherapp.model.MainPresenterModel;
import com.adroitandroid.weatherapp.view.MainView;

/**
 * Created by pv on 25/06/17.
 */

public class MainPresenter extends XPresenter<MainView, MainPresenterModel> {

    public static final String INFO_MESSAGE = "Please enter your name to display here";
    public static final String ERROR_MESSAGE = "Please enter a valid name";

    public void submitInput(String input) {
        if (input == null || input.length() == 0) {
            getView().setError(ERROR_MESSAGE);
        } else {
            getPresenterModel().setName(input);
            getView().setText(input);
        }
    }

    public String getText() {
        String name = getPresenterModel().getName();
        if (name == null) {
            return INFO_MESSAGE;
        } else {
            return name;
        }
    }
}
