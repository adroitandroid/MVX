package com.adroitandroid.mvx;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by pv on 25/06/17.
 */

public abstract class XPresenterModel<xView extends XView> extends IntentService {

    private boolean mIsDisposable;

    public XPresenterModel(String presenterModelName) {
        super(presenterModelName);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.log("New instance of service started");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mIsDisposable = false;
        while (!mIsDisposable) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.log(e.getLocalizedMessage());
            }
            Log.log("Presenter isn't disposable now");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.log("presenter onBind called");
        return getPresenter().setPresenterModel(this);
    }

    public abstract <xPresenterModel extends XPresenterModel<xView>> XPresenter<xView, xPresenterModel> getPresenter();

    public void setDisposable() {
        mIsDisposable = true;
        Log.log("Presenter should be disposable again");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.log("Presenter destroyed!");
    }
}
