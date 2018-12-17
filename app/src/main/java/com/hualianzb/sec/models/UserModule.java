package com.hualianzb.sec.models;

import com.hualianzb.sec.application.SECApplication;

import java.io.Serializable;

/**
 * 用户模块 json解析
 * Created by Administrator on 2015/12/7 0007.
 */
public class UserModule implements Serializable {
    private static UserModule instance = null;

    public UserModule() {
    }

    public static UserModule getInstance() {
        if (instance == null) {
            synchronized (SECApplication.class) {
                if (instance == null) {
                    instance = new UserModule();
                }
            }
        }
        return instance;
    }

    public static void setInstance(UserModule instance) {
        UserModule.instance = instance;
    }


}
