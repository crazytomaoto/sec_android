package com.hualianzb.sec.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hualianzb.sec.R;
import com.hualianzb.sec.interfaces.DialogControl;
import com.hualianzb.sec.models.UserModule;
import com.hualianzb.sec.utils.SharedPreferencesUtils;
import com.hualianzb.sec.views.CustomProgressDialog;
import com.hualianzb.sec.views.WaitDialog;
import com.hysd.android.platform_huanuo.base.manager.MessageCenter;
import com.zhy.autolayout.AutoLayoutActivity;

import me.leefeng.promptlibrary.PromptDialog;


/**
 * BaseActivity
 * Created by wty on 2018/4/2.
 */
public class BaseActivity extends AutoLayoutActivity implements DialogControl {
    public static final int TIME_OUT_INT = 15 * 1000;
    private CustomProgressDialog mProgressDialog;
    public static UserModule userModule;
    public static SharedPreferencesUtils spUtils;
    private AnimationDrawable animationDrawable;
    public static PromptDialog promptDialog;
    protected static View mTipView;
    protected static WindowManager mWindowManager;
    protected static WindowManager.LayoutParams mLayoutParams;

    /**
     * 返回键
     *
     * @param v view
     */
    public void back(View v) {
        this.finish();
        hideInput();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtils = new SharedPreferencesUtils(this, "user_info.spf");
        userModule = spUtils.getObject("user_module.spf");
        _isVisible = true;
        promptDialog = new PromptDialog(this);
        initTipView();
    }


    private void initTipView() {
        LayoutInflater inflater = getLayoutInflater();
        mTipView = inflater.inflate(R.layout.layout_network_tip, null); //提示View布局
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        //使用非CENTER时，可以通过设置XY的值来改变View的位置
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mTipView.setVisibility(View.VISIBLE);
    }

    /**
     * 检查当前网络是否可用
     *
     * @param activity
     * @return
     */

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * show loading progress dialog
     */
    public void showDialog() {
        mProgressDialog = new CustomProgressDialog(this, R.layout.progress_dialog, R.style.Theme_dialog);
//        ImageView iv_voice_animation = (ImageView) mProgressDialog.findViewById(R.id.iv_voice_animation);
//        iv_voice_animation.setImageResource(R.drawable.load_animation);
//        animationDrawable = (AnimationDrawable) iv_voice_animation.getDrawable();
//        animationDrawable.start();
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mProgressDialog.setOnKeyListener(onKeyListener);

        //5秒后自动关闭
        MessageCenter messageCenter = MessageCenter.getInstance();
        messageCenter.sendEmptyMesageDelay(100, 5000);
    }

    /**
     * add a key listener for progress dialog
     */
    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                dismissDialog();
            }
            return false;
        }
    };

    /**
     * 键盘弹出
     *
     * @param editText
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideInput() {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != BaseActivity.this.getCurrentFocus())
                inputMethodManager.hideSoftInputFromWindow(
                        BaseActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * cancel progress dialog if necessary
     */
    @Override
    public void onBackPressed() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * dismiss dialog
     */
    public void dismissDialog() {
        if (isFinishing()) {
            return;
        }
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            mProgressDialog.dismiss();
        }

    }

    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
    }

    private WaitDialog _waitDialog;

    private boolean _isVisible;

    @Override
    public void hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public WaitDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public WaitDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = getWaitDialog(this, message);
            }
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }

    private WaitDialog getWaitDialog(Activity activity, String message) {
        WaitDialog dialog = null;
        try {
            dialog = new WaitDialog(activity, R.style.dialog_waiting);
            dialog.setMessage(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }


}