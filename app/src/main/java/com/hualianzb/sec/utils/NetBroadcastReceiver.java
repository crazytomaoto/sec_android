package com.hualianzb.sec.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.NetUtil;

/**
 * Date:2018/10/24
 * auther:wangtianyun
 * describe:监听网络变化的广播
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetChangeListener listener = BasicActivity.listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
            if (listener != null) {
                listener.onChangeListener(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetChangeListener {
        void onChangeListener(int status);
    }

}