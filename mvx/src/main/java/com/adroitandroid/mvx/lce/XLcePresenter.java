package com.adroitandroid.mvx.lce;

import com.adroitandroid.mvx.XPresenter;

/**
 * Created by pv on 25/06/17.
 */

public abstract class XLcePresenter<T, vView extends XLceView<T>, vPresenterModel extends XLcePresenterModel<T, vView>>
        extends XPresenter<vView, vPresenterModel> {

    private Long mId;

    public long getId() {
        if (mId == null) {
            mId = System.currentTimeMillis();
        }
        return mId;
    }

    public void startFetch() {
        onStartFetch();
        getView().onFetchStart();
    }

    protected void setError(String error) {
        onFetchError(error);
        getView().onError(error);
    }

    protected void complete(T data) {
        onFetchComplete(data);
        getView().onContentReady(data);
    }

    protected abstract void onFetchComplete(T data);

    protected abstract void onFetchError(String error);

    protected abstract void onStartFetch();

}
