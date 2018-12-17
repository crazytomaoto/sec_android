package com.hualianzb.sec.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.interfaces.DialogControl;

/**
 * Created by wty on 2017/8/22.
 */

public class WaitDialog extends Dialog {

    private TextView _messageTv;

    public WaitDialog(Context context) {
        super(context);
        init(context);
    }

    public WaitDialog(Context context, int defStyle) {
        super(context, defStyle);
        init(context);
    }

    protected WaitDialog(Context context, boolean cancelable, OnCancelListener listener) {
        super(context, cancelable, listener);
        init(context);
    }

    public static boolean dismiss(WaitDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }

    public static void hide(Context context) {
        if (context instanceof DialogControl)
            ((DialogControl) context).hideWaitDialog();
    }

    public static boolean hide(WaitDialog dialog) {
        if (dialog != null) {
            dialog.hide();
            return false;
        } else {
            return true;
        }
    }

    private void init(Context context) {
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_wait, null);
        _messageTv = (TextView) view.findViewById(R.id.waiting_tv);
        setContentView(view);
    }

    public static void show(Context context) {
        if (context instanceof DialogControl)
            ((DialogControl) context).showWaitDialog();
    }

    public static boolean show(WaitDialog waitdialog) {
        boolean flag;
        if (waitdialog != null) {
            waitdialog.show();
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (isTablet()) {
            int i = (int) dpToPixel(360F);
            if (i < getScreenWidth()) {
                WindowManager.LayoutParams params = getWindow()
                        .getAttributes();
                params.width = i;
                getWindow().setAttributes(params);
            }
        }
    }

    public void setMessage(int message) {
        _messageTv.setText(message);
    }

    public void setMessage(String message) {
        _messageTv.setText(message);
    }

    public void hideMessage() {
        _messageTv.setVisibility(View.GONE);
    }

    private Boolean _isTablet = null;

    private boolean isTablet() {
        if (_isTablet == null) {
            boolean flag;
            if ((0xf & getContext().getResources().getConfiguration().screenLayout) >= 3)
                flag = true;
            else
                flag = false;
            _isTablet = Boolean.valueOf(flag);
        }
        return _isTablet.booleanValue();
    }

    private float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) SECApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(displaymetrics);
        return displaymetrics;
    }

    private float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }


}