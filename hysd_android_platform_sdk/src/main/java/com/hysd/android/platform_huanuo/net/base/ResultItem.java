package com.hysd.android.platform_huanuo.net.base;

import android.text.TextUtils;
import android.util.Base64;

import com.hysd.android.platform_huanuo.utils.Logger;
import com.hysd.android.platform_huanuo.utils.NumberUtils;
import com.hysd.android.platform_huanuo.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * FileName    : ResultItem.java
 * Description : 一个工动态bean的处理类
 *
 * @author : 刘剑
 * @version : 1.0
 *          Create Date : 2014-4-9 下午2:16:27
 * @Copyright : hysdpower. All Rights Reserved
 * @Company :
 **/
@SuppressWarnings("all")
public class ResultItem implements Serializable {

    private static final String TAG = "ResultItem";

    private static final long serialVersionUID = -4895681135228175505L;

    private String jsonContent;

    /**
     * 数据载体
     */
    private Map<String, Object> items = new HashMap<String, Object>();

    public ResultItem() {

    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public ResultItem(HashMap<String, Object> mapObj) {
        items.putAll(mapObj);
    }

    public ResultItem(JSONObject jsonObj) {
        if (jsonObj != null) {
            Iterator keys = jsonObj.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                try {
                    Object obj = jsonObj.get(key);
                    if (obj != null)
                        if (obj instanceof JSONObject) {
                            put(key, new ResultItem((JSONObject) obj));
                        } else if (obj instanceof JSONArray) {
                            List listItems = new ArrayList();

                            JSONArray tempArray = (JSONArray) obj;
                            for (int i = 0; i < tempArray.length(); i++) {
                                Object itempObj = tempArray.get(i);
                                if (itempObj instanceof JSONObject) {
                                    listItems.add(new ResultItem(tempArray.getJSONObject(i)));
                                } else
                                    listItems.add(itempObj);
                            }
                            put(key, listItems);
                        } else {
                            put(key, obj.toString());
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加数据
     */
    public void put(String key, Object obj) {
        this.items.put(key.toUpperCase(), obj);
    }

    /**
     * 获取值
     *
     * @param key key
     * @param <T> 类名
     * @param t   Class<T>
     * @return T
     */
    public <T> T get(String key, Class<T> t) {
        try {
            Object object = get(key);
            return (T) object;
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return null;
    }

    /**
     * 获取集合
     *
     * @param key
     * @return
     */
    public List<ResultItem> getItems(String key) {
        try {
            Object object = get(key);
            if (object != null) {
                if (object instanceof ResultItem) {
                    return Arrays.asList((ResultItem) object);
                } else if (object instanceof List<?>) {
                    List<?> objs = (List<?>) object;
                    if (objs.size() > 0 && objs.get(0) instanceof ResultItem) {
                        return (List<ResultItem>) object;
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return null;
    }

    /**
     * xpath查找
     *
     * @param key key
     * @return Object
     */
    public Object get(String key) {
        if (!TextUtils.isEmpty(key)) {
            if (key.indexOf("|") > 0) {
                String[] parts = key.split("\\|");
                Object obj = null;
                boolean isGet = false;
                for (String tempPart : parts) {
                    if (obj == null) {
                        if (isGet) {
                            break;
                        } else {
                            obj = get(tempPart);
                            isGet = true;
                        }
                    } else {
                        if (obj != null) {
                            if (obj instanceof ResultItem) {
                                obj = ((ResultItem) obj).get(tempPart);
                            }
                        }
                    }
                }
                if (obj != null) {
                    return obj;
                }
            }
        }
        return items.get(key.toUpperCase());
    }

    /**
     * 获取base64反转的字符串
     *
     * @param key
     * @return
     */
    public String getBase64(String key) {
        return getBase64(key, "");
    }

    /**
     * 获取base64反转的字符串
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return String
     */
    public String getBase64(String key, String defaultValue) {
        String msg = defaultValue;
        try {
            Object obj = get(key);
            if (obj == null || obj.equals("null")) {
                obj = "";
            }
            msg = obj.toString();
            if (obj != null) {
                byte[] bText = Base64.decode(msg.toString(), Base64.DEFAULT);
                return new String(bText, "UTF8");
            }
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return msg;
    }

    /**
     * 获取数据 String
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return String
     */
    public String getString(String key, String defaultValue) {
        String msg = defaultValue;
        try {
            Object obj = get(key);
            msg = obj == null ? "" : obj.toString();
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return msg;
    }

    /**
     * 获取数据 String
     *
     * @param key key
     * @return String
     */
    public String getString(String key) {
        if(!StringUtils.isNEmpty(getString(key, ""))){
            return getString(key, "");
        }else{
            return "";
        }
    }

    /**
     * 获取数据 int
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return int
     */
    public int getInt(String key, int defaultValue) {
        int msg = defaultValue;
        try {
            Object obj = get(key);
            if (obj != null && !"null".equals(obj)) {
                if (obj instanceof Integer) {
                    msg = (Integer) obj;
                } else {
                    if (!StringUtils.isNEmpty(obj.toString())) {
                        double d = Double.parseDouble(obj.toString());
                        msg = Integer.parseInt(new java.text.DecimalFormat("0").format(d));
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return msg;
    }

    /**
     * 获取数据 int
     *
     * @param key key
     * @return int
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 获取数据 long
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return long
     */
    public long getLong(String key, long defaultValue) {
        long msg = defaultValue;
        try {
            Object obj = get(key);
            if (obj != null && !"null".equals(obj)) {
                if (obj instanceof Long) {
                    msg = (Long) obj;
                } else {
                    msg = Long.valueOf(obj.toString());
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return msg;
    }

    /**
     * 获取数据 long
     *
     * @param key key
     * @return long
     */
    public long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     * 获取数据 float
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return float
     */
    public float getFloat(String key, float defaultValue) {
        float msg = defaultValue;
        try {
            Object obj = get(key);
            if (obj != null && !"null".equals(obj)) {
                if (obj instanceof Float) {
                    msg = (Float) obj;
                } else {
                    msg = Float.valueOf(obj.toString());
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return msg;
    }

    /**
     * 获取数据 float
     *
     * @param key key
     * @return float
     */
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    /**
     * 获取数据 Double
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return Double
     */
    public Double getDouble(String key, double defaultValue) {
        double msg = defaultValue;
        try {
            Object obj = get(key);
            if (obj != null && !"null".equals(obj)) {
                if (obj instanceof Double) {
                    msg = (Double) obj;
                } else {
                    msg = Double.valueOf(obj.toString());
                }
                msg = NumberUtils.round(msg, 2);
            }
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return msg;
    }

    /**
     * 获取数据 Double
     *
     * @param key key
     * @return Double
     */
    public Double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * 获取数据 Boolean
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return Boolean
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        boolean msg = defaultValue;
        try {
            Object obj = get(key);
            if (obj != null) {
                msg = Boolean.valueOf(obj.toString());
            }
        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
        }
        return msg;
    }

    /**
     * 获取数据 Boolean
     *
     * @param key key
     * @return Boolean
     */
    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 获取枚举值
     *
     * @param key key
     * @return int
     */
    public int getEnumValue(String key) {
        return getEnumValue(key, 0);
    }

    /**
     * 获取枚举值
     *
     * @param key key
     * @return int
     */
    public int getEnumValue(String key, int defaultValue) {
        ResultItem enumItem = (ResultItem) get(key);
        if (enumItem != null) {
            return enumItem.getInt("val");
        } else {
            return defaultValue;
        }
    }
}
