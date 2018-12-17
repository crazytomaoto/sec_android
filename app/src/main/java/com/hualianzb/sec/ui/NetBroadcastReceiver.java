package com.hualianzb.sec.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Date:2018/10/24
 * auther:wangtianyun
 * describe:监听网络变化的广播
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    private final static String TAG = "NetBroadcastReceiver";
    public static NetEvevt evevt;

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
                if (evevt != null) {
                    evevt.onNetChange(true);
                }
            } else if (!wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                if (evevt != null) {
                    evevt.onNetChange(false);
                }
            }
            //API大于23时使用下面的方式进行网络监听
        } else {
            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            if (null != networks && networks.length > 0) {
                if (evevt != null) {
                    evevt.onNetChange(true);
                }
            } else {
                if (evevt != null) {
                    evevt.onNetChange(false);
                }
            }
        }
    }

    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(boolean netMobile);
    }

    public static void setEvevt(NetEvevt evevt1) {
        evevt = evevt1;
    }
}
