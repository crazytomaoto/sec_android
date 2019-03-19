package com.hualianzb.sec.ui.basic;

import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.utils.NetBroadcastReceiver;
import com.hualianzb.sec.ui.base.BaseActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hysd.android.platform_huanuo.base.manager.MessageCenter;

import java.util.Set;


/**
 * Created by wty on 2018/4/2.
 */
public abstract class BasicActivity extends BaseActivity implements View.OnClickListener, NetBroadcastReceiver.NetChangeListener {

    private Handler mHandler;
    public static NetBroadcastReceiver.NetChangeListener listener;
    private NetBroadcastReceiver netBroadcastReceiver;
    private Dialog noNet;

    /**
     * 网络类型
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SECApplication.getInstance().addActivity(this);//应用退出

        if (null != savedInstanceState) {
            getIntentForSavedInstanceState(savedInstanceState);
        } else {
            getIntentForBundle();
        }
        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initLogics();
        MessageCenter.getInstance().addHandler(getHandler());
        listener = this;
        //Android 7.0以上需要动态注册
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netBroadcastReceiver = new NetBroadcastReceiver();
        //注册广播接收
        registerReceiver(netBroadcastReceiver, filter);
        noNet = DialogUtil.showNoNetDialog(this);
    }

    public void onChangeListener(int status) {
        if (status == -1) {
            show();
        } else {
            dismissDialog();
        }
    }

    public void show() {
        if (null == noNet) {
            noNet = DialogUtil.showNoNetDialog(this);
            if (!noNet.isShowing()) {
                noNet.show();
            }
        } else {
            if (!noNet.isShowing()) {
                noNet.show();
            }
        }
    }

    public void dismissDialog() {
        if (null != noNet && noNet.isShowing()) {
            noNet.dismiss();
        }
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 消息处理
     */
    protected void handleStateMessage(Message message) {
    }

    /**
     * logic
     */
    protected void initLogics() {
    }

    /**
     * handler
     */
    protected Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    handleStateMessage(msg);
//                    handleKickOutUnline(msg);
                }
            };
        }
        return mHandler;
    }


    /**
     * 被IM踢出 的方法
     *
     * @param message
     */
    private void handleKickOutUnline(Message message) {
        if (message.what == GlobalMessageType.CommonMessageType.USER_TOKEN_EXPIRE) {
//            myDialog = new DialogLogoutView(this, itemsOnClick);
//            myDialog.setContent("您的账号已在别的设备上登录，若不是本人操作，请及时检查账号密码是否泄露！");
//            myDialog.setDialogCallback(dialogcallback);
//            myDialog.show()
        }
    }

    protected void sendMessage(Message message) {
        getHandler().sendMessage(message);
    }

    protected void sendMessage(int what, Object obj) {
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        getHandler().sendMessage(message);
    }

    protected void sendEmptyMessage(int what) {
        getHandler().sendEmptyMessage(what);
    }

    protected void sendEmptyMessageDelayed(int what, long delayMillis) {
        getHandler().sendEmptyMessageDelayed(what, delayMillis);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        // 移除回收的handler
        MessageCenter.getInstance().removeHandler(getHandler());
        if (isImmersionBarEnabled()) {
            ImmersionBar.with(this).destroy();
        }
        super.onDestroy();
        unregisterReceiver(netBroadcastReceiver);
    }


    /**
     * 设置mydialog需要处理的事情
     */
//    DialogLogoutView.Dialogcallback dialogcallback = new DialogLogoutView.Dialogcallback() {
//        @Override
//        public void dialogdo(String string) {
//            //tv_title.setText("您的个人资料不全，无法匹配到班级，现在完善资料？"+string);
//        }
//    };
    /**
     * 自定义为弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.tv__ok:
//                    myDialog.dismiss();
////                    Intent intent = new Intent(getApplicationContext(), LoginActicity.class);
////                    intent.putExtra("viewPagerMe", 1);
////                    startActivity(intent);
//                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 需要和getIntentForBundle一起使用
     */
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {

    }

    /**
     * 需要和getIntentForSavedInstanceState一起使用
     */
    protected void getIntentForBundle() {

    }

    protected void onSaveIntent(Bundle savedInstanceState) {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Set<String> keySet = bundle.keySet();
                for (String key : keySet) {
                    Object value = bundle.get(key);
                    if (value != null) {
                        if (value instanceof String) {
                            savedInstanceState.putString(key, (String) value);
                        } else if (value instanceof Integer) {
                            savedInstanceState.putInt(key, (Integer) value);
                        } else if (value instanceof Boolean) {
                            savedInstanceState.putBoolean(key, (Boolean) value);
                        } else if (value instanceof Float) {
                            savedInstanceState.putFloat(key, (Float) value);
                        } else if (value instanceof Long) {
                            savedInstanceState.putLong(key, (Long) value);
                        } else {
                            savedInstanceState.putString(key, value.toString());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        onSaveIntent(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {

        }
        getIntentForSavedInstanceState(savedInstanceState);
    }

}
