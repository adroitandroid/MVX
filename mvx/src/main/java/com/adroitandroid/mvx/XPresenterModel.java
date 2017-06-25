package com.adroitandroid.mvx;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by pv on 25/06/17.
 */

public abstract class XPresenterModel<xView extends XView> extends IntentService {

    private boolean mIsDisposable;

    public XPresenterModel() {
        super("Presenter[" + System.currentTimeMillis() + "]");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MVX", "New instance of service started");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mIsDisposable = false;
        while (!mIsDisposable) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("MVX", e.getLocalizedMessage());
            }
            Log.d("MVX", "Presenter isn't disposable now");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MVX", "presenter onBind called");
        return getPresenter().setPresenterModel(this);
    }

    public abstract <xPresenterModel extends XPresenterModel<xView>> XPresenter<xView, xPresenterModel> getPresenter();

    public void setDisposable() {
        mIsDisposable = true;
        Log.d("MVX", "Presenter should be disposable again");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MVX", "Presenter destroyed!");
    }
}
