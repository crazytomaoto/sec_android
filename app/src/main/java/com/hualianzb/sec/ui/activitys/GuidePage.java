package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

public class GuidePage extends BasicActivity {
    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    private Map<String, RememberSEC> map;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_LAUNCHERACTIVITY:
                    //跳转到MainActivity，并结束当前的LauncherActivity
                    initData();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        // 停留3秒后发送消息，跳转到MainActivity
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 2000);
    }

    private void initData() {
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            UiHelper.startActyCreateInsertWallet(this);
        } else {
            for (RememberSEC rememberEth : map.values()) {
                if (rememberEth.isNow()) {//查找选定的钱包
                    UiHelper.startHomaPageAc(this, rememberEth.getAddress());
                    break;
                }
            }
        }
    }
}
