package com.hualianzb.sec.interfaces;

import com.hualianzb.sec.views.WaitDialog;

/**
 * Created by wty on 2017/8/22.
 */

public interface DialogControl {
    void hideWaitDialog();
    WaitDialog showWaitDialog();
    WaitDialog showWaitDialog(int resid);
    WaitDialog showWaitDialog(String text);
}
