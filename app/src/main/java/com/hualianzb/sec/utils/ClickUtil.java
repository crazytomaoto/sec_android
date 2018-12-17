package com.hualianzb.sec.utils;

/**
 * 为了防止用户或者测试MM疯狂的点击某个button
 * Created by Administrator on 2018/5/17.
 */
public class ClickUtil {
    private static long lastClickTime;

    public static boolean isNotFirstClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
