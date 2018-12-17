package com.hualianzb.sec.commons.cache;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/2 0002.
 */
public class ImageCache {

    public static Map<String, String> HEAD_IMAGS_CACHE = new HashMap<String, String>();


    public static String getImageUrlById(String ningmeng) {
        String url = HEAD_IMAGS_CACHE.get(ningmeng);

        return TextUtils.isEmpty(url) ? "" : url;

    }

//    public static void cacheImageList(List<MessageItem> message, Context context) {
//        if (message != null && message.size() > 0) {
//            for (int i = 0; i < message.size(); i++) {
//                MessageItem messageItem = message.get(i);
//
//                String key = "";
//                if (messageItem.isComMeg()) {
//                    key = messageItem.getId();
//                } else {
//                    SharedPreferences s1 = context.getSharedPreferences("phone", 1);
//                    key = s1.getString("icon", "");
//                }
//                HEAD_IMAGS_CACHE.put(key, messageItem.getHeadImg());
//            }
//        }
//    }
}
