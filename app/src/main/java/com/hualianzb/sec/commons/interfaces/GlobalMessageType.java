package com.hualianzb.sec.commons.interfaces;

public interface GlobalMessageType {

    /**
     * 通用模块消息类型
     */
    interface CommonMessageType {
        int BASE = 0x10000000;
        /**
         * 用户Token过期
         */
        int USER_TOKEN_EXPIRE = BASE + 1;
        int GETPICTURE = BASE + 2;//获取图片

        int REMOVELIST = BASE + 163;//适配器中要清除本条数据
    }

    interface MainRequest {
        int BASE = 0x11000000;
        int CECTOKEN = BASE + 1;//                              +++ ece token值 +++
        int ETHTOKEN = BASE + 2;//
        int SECTOKEN = BASE + 3;//
        int INTTOKEN = BASE + 4;//
        int GETTOKEN = BASE + 5;// token值
        int CECTOKEN_LAST = BASE + 11;//                              +++ ece token值 +++
        int ETHTOKEN_LAST = BASE + 12;//
        int SECTOKEN_LAST = BASE + 13;//
        int INTTOKEN_LAST = BASE + 14;//
        int GETTOKEN_LAST = BASE + 6;//token值
    }

    interface RequestCode {
        int BASE = 1000;
        int FromCreate = BASE + 1;
        int FromMnFragment = BASE + 2;
        int FromOfficialFragment = BASE + 3;
        int FromPrivatKey = BASE + 4;
    }

    interface MessgeCode {
        int BASE = 0x12000000;
        int NOTIFYBOOKLIST = BASE + 1;//地址簿改变后通知列表刷新
        int NONET = BASE + 2;//没网
    }

}