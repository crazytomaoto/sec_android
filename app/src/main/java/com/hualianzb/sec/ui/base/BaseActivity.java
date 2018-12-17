package com.hualianzb.sec.ui.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.interfaces.DialogControl;
import com.hualianzb.sec.models.UserModule;
import com.hualianzb.sec.ui.NetBroadcastReceiver;
import com.hualianzb.sec.utils.NetUtil;
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
public class BaseActivity extends AutoLayoutActivity implements DialogControl, NetBroadcastReceiver.NetEvevt {
    public static final int TIME_OUT_INT = 15 * 1000;
    private CustomProgressDialog mProgressDialog;
    public static UserModule userModule;
    public static SharedPreferencesUtils spUtils;
    private AnimationDrawable animationDrawable;
    public static PromptDialog promptDialog;
    protected boolean mCheckNetWork = true; //默认检查网络状态
    protected static View mTipView;
    protected static WindowManager mWindowManager;
    protected static WindowManager.LayoutParams mLayoutParams;
    protected boolean netMobile;
    NetBroadcastReceiver receiver;

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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                setTranslucentStatus(true);
//                SystemBarTintManager tintManager = new SystemBarTintManager(this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.blue_background);//通知栏所需颜色
//
//        }
        initTipView();
        receiver = new NetBroadcastReceiver();
        evevt = this;
        receiver.setEvevt(evevt);
//        boolean isConnectes = inspectNet();
        spUtils = new SharedPreferencesUtils(this, "user_info.spf");
        userModule = spUtils.getObject("user_module.spf");
        _isVisible = true;
        promptDialog = new PromptDialog(this);

    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void onAttach(Activity activity) {

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

    public boolean isCheckNetWork() {
        return mCheckNetWork;
    }

    private void hasNetWork(boolean has) {
        if (isCheckNetWork()) {
            if (has) {
                if (mTipView != null && mTipView.getParent() != null) {
                    mWindowManager.removeView(mTipView);
                }
            } else {
                if (mTipView.getParent() == null) {
                    mWindowManager.addView(mTipView, mLayoutParams);
                }
            }
        }
    }

    public static void initLineanlayout(boolean has) {
        if (has) {
            if (mTipView != null && mTipView.getParent() != null) {
                mWindowManager.removeView(mTipView);
            }
        } else {
            if (mTipView.getParent() == null) {
                mWindowManager.addView(mTipView, mLayoutParams);
            }
        }
    }

    private void initTipView() {
        LayoutInflater inflater = getLayoutInflater();
        mTipView = inflater.inflate(R.layout.layout_network_tip, null); //提示View布局
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        //使用非CENTER时，可以通过设置XY的值来改变View的位置
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = 106;
        hasNetWork(NetUtil.getNetWorkState(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
    }

    public void DialogAll() {
        //弹出框
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.layout_dialog_warntip);
        TextView ok = (TextView) window.findViewById(R.id.tv__ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        TextView cancel = (TextView) window.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
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

    public static NetBroadcastReceiver.NetEvevt evevt;


    @Override
    public void onNetChange(boolean netMobile) {
        doSomeUI(netMobile);
    }
    //检测网络变化情况后对应显示布局
    protected void doSomeUI(boolean netMobile) {
        initLineanlayout(netMobile);
    }

}