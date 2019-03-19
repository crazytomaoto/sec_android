package com.hualianzb.sec.application;

import android.app.Activity;
import android.content.Context;

import com.hualianzb.sec.BuildConfig;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.PropertyBean;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.secUtil.SECBlockJavascriptAPI;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.base.manager.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther:Created by wangtianyun on 2018/4/2.
 * des:
 */

public class SECApplication extends BaseApplication {
    private static SECApplication instance = null;
    public static List<Activity> activityList = new LinkedList<>();
    private static Context mContext;
    public Map<String, RememberSEC> map = new HashMap<>();
    public List<PropertyBean> listToken = new ArrayList<>();
    public static SECBlockJavascriptAPI secJsApi;

    public static synchronized Context getContext() {
        return mContext;
    }

    public SECApplication() {
    }

    @Override
    public void onCreate() {
        try {
            mContext = getApplicationContext();
            AutoLayoutConifg.getInstance().useDeviceSize();
            CrashReport.initCrashReport(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        super.onCreate();
        initKind();
//        对xUtils进行初始化
        x.Ext.init(this);
        //是否是开发、调试模式
        x.Ext.setDebug(BuildConfig.DEBUG);//是否输出debug日志，开启debug会影响性能
        //初始化api
        try {
            secJsApi = new SECBlockJavascriptAPI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initKind() {
        boolean isFirstLogin = PlatformConfig.getBoolean(Constant.SpConstant.FIRST);
        if (isFirstLogin == false) {
            PropertyBean bean = new PropertyBean();
            bean.setName(Constant.SpConstant.SEC);
            bean.setAllName("Social Ecommerce Chain");
            bean.setTokenAddress(RequestHost.secTestUrl);
            bean.setChecked(true);
            listToken.add(bean);
            PlatformConfig.putList(Constant.SpConstant.KINDTOKEN, listToken);
        }
    }

    public static SECApplication getInstance() {
        if (instance == null) {
            synchronized (SECApplication.class) {
                if (instance == null) {
                    instance = new SECApplication();
                }
            }
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */

    public void finishActivity(Activity activity) {

        if (activity != null) {
            this.activityList.remove(activity);
            activity.finish();
        }
    }

    /**
     * 应用退出，清理所有活动
     */
    public void exit() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    public void setMap(RememberSEC rememberEth) {
        map.put(rememberEth.getAddress(), rememberEth);
    }

}