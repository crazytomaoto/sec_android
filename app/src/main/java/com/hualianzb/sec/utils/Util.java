package com.hualianzb.sec.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;

import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.AddressBookBean;
import com.hualianzb.sec.models.AllTransBean;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.models.SecTransactionBean;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    //左边第一位不为0的脚标
    public static int getIndexNoneZore(String num) {
        char[] temp = num.toCharArray();
        int index = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != '0') {
                index = i;
                break;
            }
        }
        return index;
    }

    public static String get10Time(String time) {
        String timeLast = time.substring(2);//16进制去掉0x
        BigInteger d = new BigInteger(timeLast, 16);
        Long ddd = Long.parseLong(d.toString(10));//单位秒
        String date = ddd * 1000 + "";
//        TimeUtil.getTime12(ddd);
        return date;
    }

    public static List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listSortRecord(List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> list) {
        Collections.sort(list, (o1, o2) -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {

                long t1 = o1.getTimeStamp();
                long t2 = o2.getTimeStamp();

                String t11 = TimeUtil.getTime1(t1);
                String t22 = TimeUtil.getTime1(t2);

                Date dt1 = format.parse(t11);
                Date dt2 = format.parse(t22);
                if (dt1.getTime() < dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() > dt2.getTime()) {
                    return -1;
                } else {
                    return 0;
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return 0;

        });
        return list;
    }

    /**
     * 储存每个钱包对应的币种
     *
     * @param myAddr------钱包的地址
     */
    public static void saveTokenKindsForEacthWallet(String myAddr) {
        Map<String, ArrayList<String>> mapTokens = PlatformConfig.getMap(Constant.SpConstant.ALLKINDTOKEN);
        if (null == mapTokens) {
            mapTokens = new HashMap<>();
        }
        ArrayList<String> listNames = new ArrayList<>();
//        listNames.add("ETH");
//        listNames.add("CEC");
        listNames.add("SEC");
        mapTokens.put(myAddr, listNames);
        PlatformConfig.putMap(Constant.SpConstant.ALLKINDTOKEN, mapTokens);
    }

    public static List<AddressBookBean> listAddressBookSort(List<AddressBookBean> list) {
        Collections.sort(list, new Comparator<AddressBookBean>() {
            @Override
            public int compare(AddressBookBean o1, AddressBookBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getCreatTime());
                    Date dt2 = format.parse(o2.getCreatTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;

                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;

                    } else {
                        return 0;
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
                return 0;

            }
        });
        return list;
    }

    public static boolean isListEqual(List l0, List l1) {
        if (l0 == l1)
            return true;
        if (l0 == null && l1 == null)
            return true;
        if (l0 == null || l1 == null)
            return false;
        if (l0.size() != l1.size())
            return false;
        for (Object o : l0) {
            if (!l1.contains(o))
                return false;
        }
        for (Object o : l1) {
            if (!l0.contains(o))
                return false;
        }
        return true;
    }

    public static List<RememberSEC> ListSort(List<RememberSEC> list) {
        Collections.sort(list, new Comparator<RememberSEC>() {
            @Override
            public int compare(RememberSEC o1, RememberSEC o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getCreatTime());
                    Date dt2 = format.parse(o2.getCreatTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;

                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;

                    } else {
                        return 0;
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
                return 0;

            }
        });
        return list;
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());

        return (int) scale;
    }


    /**
     * 根据uri  获取 bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    public static Map<String, String> getTransRemarks() {
        Map<String, String> map = PlatformConfig.getMap(Constant.SpConstant.TRANSREMARKS);
        return map;
    }

    public static boolean isNetworkAvailable(Context activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void copy(Activity activity, String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("content", content);
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

}
