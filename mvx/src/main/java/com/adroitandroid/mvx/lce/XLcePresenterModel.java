package com.adroitandroid.mvx.lce;

import com.adroitandroid.mvx.XPresenterModel;

/**
 * Created by pv on 25/06/17.
 */

public abstract class XLcePresenterModel<T, vView extends XLceView<T>> extends XPresenterModel<vView> {
    public XLcePresenterModel(String presenterModelName) {
        super(presenterModelName);
    }
}
