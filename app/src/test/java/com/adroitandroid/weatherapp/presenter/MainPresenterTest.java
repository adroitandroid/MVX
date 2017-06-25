package com.adroitandroid.weatherapp.presenter;

import android.content.Context;

import com.adroitandroid.weatherapp.model.MainPresenterModel;
import com.adroitandroid.weatherapp.view.MainView;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainPresenterTest {

    @Test
    public void getTextWhenNotSetInModel() {
        MainView dummyMainView = new MainView() {
            @Override
            public void setError(String error) {

            }

            @Override
            public void setText(String input) {

            }

            @Override
            public Context getContext() {
                return null;
            }
        };
        MainPresenter mainPresenter = new MainPresenterForTest(new MainPresenterModel() {
            @Override
            public String getName() {
                return null;
            }
        }, dummyMainView);
        assertEquals(MainPresenter.INFO_MESSAGE, mainPresenter.getText());
    }

    @Test
    public void getTextAsSetInModel() {
        final String text = "hakunamatata";
        MainView dummyMainView = new MainView() {
            @Override
            public void setError(String error) {

            }

            @Override
            public void setText(String input) {

            }

            @Override
            public Context getContext() {
                return null;
            }
        };
        MainPresenter mainPresenter = new MainPresenterForTest(new MainPresenterModel() {
            @Override
            public String getName() {
                return text;
            }
        }, dummyMainView);
        assertEquals(text, mainPresenter.getText());
    }

    @Test
    public void submitInputBadInput() {
        MainViewForTest mainView = new MainViewForTest();
        MainPresenter mainPresenter = new MainPresenterForTest(new MainPresenterModel(), mainView);
        assertEquals(null, mainView.mError);
        assertEquals(null, mainView.mText);
        mainPresenter.submitInput("");
        assertEquals(MainPresenter.ERROR_MESSAGE, mainView.mError);
        assertEquals(null, mainView.mText);

        MainViewForTest mainView1 = new MainViewForTest();
        MainPresenter mainPresenter1 = new MainPresenterForTest(new MainPresenterModel(), mainView1);
        assertEquals(null, mainView1.mError);
        assertEquals(null, mainView1.mText);
        mainPresenter1.submitInput(null);
        assertEquals(MainPresenter.ERROR_MESSAGE, mainView1.mError);
        assertEquals(null, mainView1.mText);
    }

    @Test
    public void submitValidInput() {
        MainViewForTest mainView = new MainViewForTest();
        MainPresenterModel presenterModel = new MainPresenterModel();
        MainPresenter mainPresenter = new MainPresenterForTest(presenterModel, mainView);
        assertEquals(null, mainView.mError);
        assertEquals(null, mainView.mText);
        assertEquals(null, presenterModel.getName());

        String text = "hakunamatata";
        mainPresenter.submitInput(text);
        assertEquals(null, mainView.mError);
        assertEquals(text, mainView.mText);
        assertEquals(text, presenterModel.getName());
    }

    private class MainPresenterForTest extends MainPresenter {
        private MainPresenterModel mPresenterModel = new MainPresenterModel();
        private MainView mMainView;

        MainPresenterForTest(MainPresenterModel presenterModel, MainView mainView) {
            this.mPresenterModel = presenterModel;
            this.mMainView = mainView;
        }

        @Override
        protected MainPresenterModel getPresenterModel() {
            return mPresenterModel;
        }

        @Override
        protected MainView getView() {
            return mMainView;
        }
    }

    private class MainViewForTest implements MainView {
        private String mError = null;
        private String mText = null;

        @Override
        public Context getContext() {
            return null;
        }

        @Override
        public void setError(String error) {
            mError = error;
        }

        @Override
        public void setText(String input) {
            mText = input;
        }
    }
}