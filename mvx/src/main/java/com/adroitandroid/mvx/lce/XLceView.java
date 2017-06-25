package com.adroitandroid.mvx.lce;

import com.adroitandroid.mvx.XView;

/**
 * Created by pv on 25/06/17.
 */

public interface XLceView<T> extends XView {

    void onContentReady(T content);

    void onError(String error);

    void onFetchStart();
}
