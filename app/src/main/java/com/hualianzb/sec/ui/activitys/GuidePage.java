package com.hualianzb.sec.ui.activitys;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import org.greenrobot.eventbus.Subscribe;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GuidePage extends BasicActivity {
    private boolean isFirstLogin;//是否首次登陆
    private boolean isAgree;//是否同意隐私条款
    private Map<String, RememberEth> map;
    private StateBarUtil stateBarUtil;
    private int time = 3;
    private Timer timer = new Timer();
    private TimerTask task = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 26:
                    initData();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();
    }

    @Subscribe
    public void onEvent(String event) {

    }

    private void initView() {
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        isFirstLogin = PlatformConfig.getBoolean(Constant.SpConstant.FIRST);
        isAgree = PlatformConfig.getBoolean(Constant.SpConstant.AGREE);
        ImageView iv = findViewById(R.id.iv_avater);
        iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo_in));
        new AsyTask().execute();
    }

    class AsyTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            // insertSMS();
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() { // UI thread
                        @Override
                        public void run() {
                            if (time <= 0) {
                                Message message = new Message();
                                message.obj = time;
                                message.what = 26;
                                handler.sendMessage(message);
                                task.cancel();
                            }
                            time--;
                        }
                    });
                }
            };
            timer.schedule(task, 0, 1000);
        }
    }

    private void initData() {
        if (false == isFirstLogin) {
            //首次登陆钱包
            isFirstLogin = true;
            PlatformConfig.setValue(Constant.SpConstant.FIRST, isFirstLogin);
            UiHelper.startServiceAgreementAct(this, true);
        } else {
            if (isAgree) {
                if (map == null || map.isEmpty() || map.values().size() == 0) {
                    UiHelper.startActyCreateInsertWallet(this);
                } else {
                    for (RememberEth rememberEth : map.values()) {
                        if (rememberEth.isNow()) {//查找选定的钱包
                            //v1.2.0开始记住当前钱包选中的钱包币种
//                            Map<String, ArrayList> mapTokens = PlatformConfig.getMap(Constant.SpConstant.ALLKINDTOKEN);
//                            //如果mapTokens为空，说明1.2.0以前的版本，要适配，要保存当前钱包的选中再去主页
//                            if (null != mapTokens && !mapTokens.values().isEmpty() && mapTokens.values().size() > 0) {
//                                ArrayList<String> myChosenKindsWalltes = mapTokens.get(rememberEth.getAddress());
//                                if (null != myChosenKindsWalltes && myChosenKindsWalltes.size() > 0) {
//                                    UiHelper.startHomaPageAc(this, rememberEth.getAddress());
//                                } else {
//                                    Util.saveTokenKindsForEacthWallet(rememberEth.getAddress());
//                                    UiHelper.startHomaPageAc(this, rememberEth.getAddress());
//                                }
//                            } else {
//                                map.clear();
//                                PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
//                                UiHelper.startActyCreateInsertWallet(this);
//                            }
                            UiHelper.startHomaPageAc(this, rememberEth.getAddress());
                            break;
                        }
                    }
                }
            } else {
                UiHelper.startServiceAgreementAct(this, true);
                finish();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (task != null) {
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
