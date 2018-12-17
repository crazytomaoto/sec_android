package com.hualianzb.sec.utils;

import android.content.Context;

import com.hualianzb.sec.commons.constants.Constant;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

public class LoginUtils {
    private static LoginUtils loginUtils = null;

    public static LoginUtils getInstance() {
        if (null == loginUtils) {
            loginUtils = new LoginUtils();
        }
        return loginUtils;
    }

    /**
     * 登录所用参数与后台约定好的，用户名--密码
     */
    public static void loginIM(final Context context) {
        String loginPhone = PlatformConfig.getString(Constant.SpConstant.USER_PHONE);
//        EMClient.getInstance().login(
//                loginPhone,
//                loginPhone + "qhb",
//                new EMCallBack() {//回调
//                    @Override
//                    public void onSuccess() {
//                        EMClient.getInstance().groupManager().loadAllGroups();
//                        EMClient.getInstance().chatManager().loadAllConversations();
//                        Logger.d("http-EMClient", "登录聊天服务器成功！");
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {
//
//                    }
//
//                    @Override
//                    public void onError(int code, String message) {
//                        ToastUtil.show(context, "登录聊天服务器失败");
//                        Logger.d("http-EMClient", "登录聊天服务器失败！");
//                    }
//                });
    }

    public static void outLoginIM(Context context) {
        //环信退出登陆
//        EMClient.getInstance().logout(true);
    }
}
