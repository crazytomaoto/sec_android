package com.hualianzb.sec.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Date:2018/10/24
 * auther:wangtianyun
 * describe:
 */
public class NetUtil {
    boolean isOn;
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;
    public static Context mContext;

    public static boolean getNetWorkState(Context context) {
        int state = -1;
        if (context == null) {
            throw new UnsupportedOperationException("please use NetUtils before init it");
        }        // 得到连接管理器对象
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                    state = NETWORK_WIFI;
                } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                    state = NETWORK_MOBILE;
                }
            } else {
                state = NETWORK_NONE;
            }
        } else {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
//            通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
//                获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo.isConnected()) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        state = NETWORK_MOBILE;
                        return (state > -1) ? true : false;
                    } else {
                        state = NETWORK_WIFI;
                        return (state > -1) ? true : false;
                    }
                }
            }
        }
        return (state > -1) ? true : false;
    }
}
