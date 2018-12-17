package com.hysd.android.platform_huanuo.base.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;

import com.hysd.android.platform_huanuo.net.base.ResultItem;
import com.hysd.android.platform_huanuo.utils.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * FileName    : PlatformConfig.java
 * Description : 一些参数的配置项
 *
 * @author : wangtianyun
 * @version : 1.0
 * Create Date : 2014-4-8 下午2:28:36
 * @Copyright : hysdpower. All Rights Reserved
 * @Company :
 **/
public class PlatformConfig {

    /**
     * http请求超时时间
     */
    public static final String HTTP_CONNECTION_TIMEOUT = "HTTP_CONNECTION_TIMEOUT";
    /**
     * http请求超时时间
     */
    public static final String HTTP_CONNECTION_READTIME = "HTTP_CONNECTION_READTIME";
    /**
     * 当前用户token
     */
    public static final String USER_TOKEN = "token";
    /**
     * 当前用户ID
     */
    public static final String USER_ID = "USER_ID";
    /**
     * 服务器请求地址
     */
    public static final String SERVICES_URL = "SERVICES_URL";
    /**
     * 服务器图片请求地址
     */
    public static final String SERVICES_URL_IMAGE = "SERVICES_URL_IMAGE";

    public static final String LOGIN_TYPE_USER = "LOGIN_TYPE_USER";
    public static final String USER_ACCOUNT = "USER_ACCOUNT";

    public static final String USER_PASSWORD = "USER_PASSWORD";
    /**
     * token有效期
     */
    public static final String TOKEN_EXPIRY = "TOKEN_EXPIRY";

    private static final String TAG = "Config";
    private static final String PLATFORM_CONFIG = "platform_config";
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static ResultItem configs = new ResultItem();

    private static Handler hanlder;

    public static void init(Context context) {
        mContext = context;
        getSharedPreferences();
        //将配置信息全部初始化到内存中
        if (mSharedPreferences != null) {
            try {
                Map<String, String> maps = (Map<String, String>) mSharedPreferences.getAll();
                for (Entry<String, String> properites : maps.entrySet()) {
                    configs.put(properites.getKey(), properites.getValue());
                }
            } catch (Exception e) {
                Logger.e(TAG, "init error:", e);
            }
        }
        hanlder = new Handler();
    }

    /**
     * 获取 SharedPreferences
     *
     * @return
     */
    private static synchronized SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null) {
            if (mContext != null) {
                mSharedPreferences = mContext.getSharedPreferences(PLATFORM_CONFIG, Context.MODE_PRIVATE);
            } else {
                Logger.e(TAG, "getSharedPreferences error:mContext is null");

            }
        }
        return mSharedPreferences;
    }

    /**
     * 获取上下文，sdk的唯一入口，不提供其他入口
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return configs.getString(key);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static String getString(String key, String defaultv) {
        return configs.getString(key, defaultv);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return configs.getInt(key);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static int getInt(String key, int defaultv) {
        return configs.getInt(key, defaultv);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        return configs.getFloat(key);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static float getFloat(String key, float defaultv) {
        return configs.getFloat(key, defaultv);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static double getDouble(String key) {
        return configs.getDouble(key);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static double getDouble(String key, double defaultv) {
        return configs.getDouble(key, defaultv);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return configs.getLong(key);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static long getLong(String key, long defaultv) {
        return configs.getLong(key, defaultv);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return configs.getBoolean(key);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultv) {
        return configs.getBoolean(key, defaultv);
    }

    /***
     * 设置参数
     * @param key
     */
    public static void remove(String key) {
        setValue(key, null);
    }

    public final static Handler getHandler() {
        return hanlder;
    }

    /**
     * 设置参数
     *
     * @param key
     * @param value
     */
    public static void setValue(String key, Object value) {
        configs.put(key, value);
        try {
            //保持到数据
            SharedPreferences mSharedPreferences = getSharedPreferences();
            Editor edit = mSharedPreferences.edit();
            if (null == value) {
                edit.remove(key);
            } else {
                if (value instanceof String) {
                    edit.putString(key, value.toString());
                } else if (value instanceof Integer) {
                    edit.putInt(key, (Integer) value);
                } else if (value instanceof Long) {
                    edit.putLong(key, (Long) value);
                } else if (value instanceof Float) {
                    edit.putFloat(key, (Float) value);
                } else if (value instanceof Boolean) {
                    edit.putBoolean(key, (Boolean) value);
                }
            }
            edit.commit();
        } catch (Exception e) {
            Logger.e(TAG, "setValue error:", e);
        }
    }

    /**
     * 存储Map集合
     *
     * @param key 键
     * @param map 存储的集合
     * @param <K> 指定Map的键
     * @param <V> 指定Map的值
     */

    public static <K extends Serializable, V extends Serializable> void putMap(

            String key, Map<K, V> map)

    {

        try {

            put(key, map);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    public static <K extends Serializable, V extends Serializable> Map<K, V> getMap(String key) {

        try {

            return (Map<K, V>) get(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    /**
     * 存储对象
     */

    private static void put(String key, Object obj)

            throws IOException

    {

        if (obj == null) {//判断对象是否为空

            return;

        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ObjectOutputStream oos = null;

        oos = new ObjectOutputStream(baos);

        oos.writeObject(obj);

        // 将对象放到OutputStream中

        // 将对象转换成byte数组，并将其进行base64编码

        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));

        baos.close();

        oos.close();

        setValue(key, objectStr);

    }


    /**
     * 获取对象
     */

    private static Object get(String key) throws IOException, ClassNotFoundException {

        String wordBase64 = getString(key);

        // 将base64格式字符串还原成byte数组

        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException

            return null;

        }

        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);

        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);

        ObjectInputStream ois = new ObjectInputStream(bais);

        // 将byte数组转换成product对象

        Object obj = ois.readObject();

        bais.close();

        ois.close();

        return obj;

    }

    public static void putList(String key, List list) {

        try {
            put(key, list);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * 获取List集合
     *
     * @param context 上下文
     * @param key     键
     * @return List集合
     */

    public static List getList(Context context, String key) {

        try {

            return (List) get(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }
}
