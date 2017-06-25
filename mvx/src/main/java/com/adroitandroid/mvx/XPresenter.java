package com.adroitandroid.mvx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by pv on 25/06/17.
 */

public abstract class XPresenter<vView extends XView, vPresenterModel extends XPresenterModel<vView>> extends Binder {

    private ServiceConnection mServiceConnection;
    private vView mView;
    private vPresenterModel mPresenterModelService;

//    TODO: checkout builder pattern or any other better way
    public static <View extends XView,
            PresenterModel extends XPresenterModel<View>,
            Presenter extends XPresenter<View, PresenterModel>>
    void bind(final View view, Class<PresenterModel> presenterModelServiceClass, final OnBindListener<Presenter> bindListener) {
        Context context = view.getContext();
        Intent intent = new Intent(context.getApplicationContext(), presenterModelServiceClass);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("MVX", "Presenter service connected");
                Presenter presenterBinder = (Presenter) service;
                presenterBinder.setServiceConnection(this);
                presenterBinder.setView(view);
                presenterBinder.restoreState();
                bindListener.onBind(presenterBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        context.getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind(Context context) {
        context.getApplicationContext().unbindService(mServiceConnection);
    }

    void setServiceConnection(ServiceConnection serviceConnection) {
        this.mServiceConnection = serviceConnection;
    }

    void setView(vView view) {
        this.mView = view;
    }

    protected vView getView() {
        return this.mView;
    }

    XPresenter<vView, vPresenterModel> setPresenterModel(vPresenterModel presenterModelService) {
        this.mPresenterModelService = presenterModelService;
        return this;
    }

    protected vPresenterModel getPresenterModel() {
        return this.mPresenterModelService;
    }

    public void saveState() {
        Context applicationContext = getView().getContext().getApplicationContext();
        Intent intent = new Intent(applicationContext, mPresenterModelService.getClass());
        applicationContext.startService(intent);
    }

    void restoreState() {
        new HandlerThread("StateRestorer") {
            @Override
            protected void onLooperPrepared() {
                Log.d("MVX", "Making presenter disposable again");
                mPresenterModelService.setDisposable();
            }
        }.start();
    }

    public interface OnBindListener<P extends XPresenter> {
        void onBind(P presenter);
    }
}
