package com.adroitandroid.weatherapp.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adroitandroid.mvx.XPresenter;
import com.adroitandroid.weatherapp.R;
import com.adroitandroid.weatherapp.databinding.ActivityMainBinding;
import com.adroitandroid.weatherapp.model.MainPresenterModel;
import com.adroitandroid.weatherapp.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainView {

    private ActivityMainBinding mDataBinding;
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        XPresenter.bind((MainView) this, MainPresenterModel.class,
                new XPresenter.OnBindListener<MainPresenter>() {
                    @Override
                    public void onBind(MainPresenter presenter) {
                        mPresenter = presenter;
                        mDataBinding.textView.setText(mPresenter.getText());
                        mDataBinding.actionBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.submitInput(mDataBinding.nameEt.getText().toString());
                            }
                        });
                    }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.saveState();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setError(String error) {
        Snackbar.make(mDataBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setText(String input) {
        mDataBinding.textView.setText(input);
    }
}
