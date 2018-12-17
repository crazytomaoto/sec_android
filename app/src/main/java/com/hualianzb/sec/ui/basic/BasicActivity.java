package com.hualianzb.sec.ui.basic;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.ui.base.BaseActivity;
import com.hysd.android.platform_huanuo.base.manager.MessageCenter;

import org.greenrobot.eventbus.Subscribe;

import java.util.Set;


/**
 * Created by wty on 2018/4/2.
 */
public abstract class BasicActivity extends BaseActivity implements View.OnClickListener {

    private Handler mHandler;
    //    private DialogLogoutView myDialog;
//    protected StatusUIManager statusUIManager;
    View mTipView;
    WindowManager mWindowManager;
    WindowManager.LayoutParams mLayoutParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SECApplication.getInstance().addActivity(this);//应用退出

        if (null != savedInstanceState) {
            getIntentForSavedInstanceState(savedInstanceState);
        } else {
            getIntentForBundle();
        }
//        initTipView();
//        EventBus.getDefault().register(this);
        initLogics();
        MessageCenter.getInstance().addHandler(getHandler());
    }

    @Subscribe
    public void onEvent(String event) {

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
        mLayoutParams.y = 0;
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
//            myDialog.show();
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
        super.onDestroy();
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

//
//    public void initStatusUI(View contentView) {
//        try {
//            statusUIManager = new StatusUIManager();
//
//            statusUIManager.addStatusProvider(
//                    new DefaultStatusProvider.DefaultLoadingStatusView(
//                            this,
//                            DefaultStatus.STATUS_LOADING,
//                            contentView,
//                            new StatusProvider.OnStatusViewCreateCallback() {
//                                @Override
//                                public void onCreate(int status, View statusView) {
//
//                                }
//                            }));
//
//            statusUIManager.addStatusProvider(
//                    new DefaultStatusProvider.DefaultEmptyStatusView(this,
//                            DefaultStatus.STATUS_EMPTY,
//                            contentView,
//                            new StatusProvider.OnStatusViewCreateCallback() {
//                                @Override
//                                public void onCreate(int status, View statusView) {
//
//                                }
//                            }));
//
//            statusUIManager.addStatusProvider(
//                    new DefaultStatusProvider.DefaultServerErrorStatusView(
//                            this,
//                            DefaultStatus.STATUS_SERVER_ERROR,
//                            contentView,
//                            new StatusProvider.OnStatusViewCreateCallback() {
//                                @Override
//                                public void onCreate(int status, View statusView) {
//
//                                }
//                            }));
//
//            statusUIManager.addStatusProvider(
//                    new DefaultStatusProvider.DefaultLogicFailStatusView(this,
//                            DefaultStatus.STATUS_LOGIC_FAIL,
//                            contentView,
//                            new StatusProvider.OnStatusViewCreateCallback() {
//                                @Override
//                                public void onCreate(int status, View statusView) {
//
//                                }
//                            }));
//
//            statusUIManager.addStatusProvider(
//                    new DefaultStatusProvider.DefaultNetOffStatusView(
//                            this,
//                            DefaultStatus.STATUS_NETOFF,
//                            contentView,
//                            new StatusProvider.OnStatusViewCreateCallback() {
//                                @Override
//                                public void onCreate(int status, View statusView) {
//
//                                }
//                            }));
//
//            statusUIManager.addStatusProvider(
//                    new DefaultStatusProvider.DefaultLocalErrorStatusView(
//                            this,
//                            DefaultStatus.STATUS_lOCAL_ERROR,
//                            contentView,
//                            new StatusProvider.OnStatusViewCreateCallback() {
//                                @Override
//                                public void onCreate(int status, View statusView) {
//
//                                }
//                            }));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

    @Override
    protected void doSomeUI(boolean netMobile) {
        super.doSomeUI(netMobile);
    }
}
