package com.adroitandroid.mvx.lce;

import com.adroitandroid.mvx.XPresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by pv on 25/06/17.
 */

public abstract class XLcePresenter<T, vView extends XLceView<T>, vPresenterModel extends XLcePresenterModel<T, vView>>
        extends XPresenter<vView, vPresenterModel> {

    public void startFetch() {
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        getView().onFetchStart();
                    }
                });
        onStartFetch();
    }

    protected void setError(final String error) {
        onFetchError(error);
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        getView().onError(error);
                    }
                });
    }

    protected void complete(final T data) {
        onFetchComplete(data);
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        getView().onContentReady(data);
                    }
                });
    }

    protected abstract void onFetchComplete(T data);

    protected abstract void onFetchError(String error);

    protected abstract void onStartFetch();

}
