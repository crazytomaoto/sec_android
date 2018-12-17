package com.hualianzb.sec.utils;

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
import com.hualianzb.sec.models.RememberEth;
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

    public static List<AllTransBean.ResultBean> ListSortRecord(List<AllTransBean.ResultBean> list) {
        Collections.sort(list, new Comparator<AllTransBean.ResultBean>() {
            @Override

            public int compare(AllTransBean.ResultBean o1, AllTransBean.ResultBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    String t1 = o1.getTimeStamp();
                    String t2 = o2.getTimeStamp();
                    if (t1.contains("0x")) {
                        t1 = get10Time(t1);
                        t1 = TimeUtil.getTimeall(Long.parseLong(t1));
                    } else {
                        t1 = TimeUtil.getTimeall(Long.parseLong(t1));
                    }

                    if (t2.contains("0x")) {
                        t2 = get10Time(t2);
                        t2 = TimeUtil.getTimeall(Long.parseLong(t2));
                    } else {
                        t2 = TimeUtil.getTimeall(Long.parseLong(t2));
                    }

                    Date dt1 = format.parse(t1);
                    Date dt2 = format.parse(t2);
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

            }
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
        listNames.add("ETH");
        listNames.add("CEC");
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

    public static List<RememberEth> ListSort(List<RememberEth> list) {
        Collections.sort(list, new Comparator<RememberEth>() {
            @Override
            public int compare(RememberEth o1, RememberEth o2) {
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

//    public static String getSubject(Context context, String subjectCode) {
//        String str = "未知科目";
//        if (StringUtils.isNotEmpty(subjectCode) && !"null".equals(subjectCode)) {
//            String subject = "00" + String.valueOf(subjectCode);
//            str = ConstantUtils.getSubjectByCode(subject);
//        }
//        return str;
//
//    }


//    public static String getGradeId(String gradeName) {
//        String[] grade = {"一年级", "二年级", "三年级", "四年级", "五年级", "六年级", "七年级", "八年级", "九年级", "高一", "高二", "高三"};
//        String[] gradeId = {"101", "102", "103", "104", "105", "106", "201", "202", "203", "301", "302", "303"};
//        for (int i = 0; i < grade.length; i++) {
//            if (grade[i].equals(gradeName)) {
//                return gradeId[i];
//            }
//        }
//        return gradeId[0];
//    }

//    public static String getGradeName(String code) {
//        String[] grade = {"一年级", "二年级", "三年级", "四年级", "五年级", "六年级", "七年级", "八年级", "九年级", "高一", "高二", "高三"};
//        String[] gradeId = {"101", "102", "103", "104", "105", "106", "201", "202", "203", "301", "302", "303"};
//        for (int i = 0; i < gradeId.length; i++) {
//            if (gradeId[i].equals(code)) {
//                return grade[i];
//            }
//        }
//        return gradeId[0];
//    }
//
//
//    public static boolean isMyFriend(Context context, String hnno) {
//        Dao dao = new Dao(context);
//        List<FriendHnno> list = dao.cccGetFriendData();
//        for (int i = 0; i < list.size(); i++) {
//            if (hnno.equals(list.get(i).getHnno())) {
//                return true;
//            }
//        }
//        return false;
//    }

//    public static String getMyHnno(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("phone", 1);
//        String hnno = sp.getString("login_hnno", null);
//        return hnno;
//    }
//
//    public static String getMyRole(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("phone", 1);
//        String hnno = sp.getString("selective_Id", null);
//        return hnno;
//    }

//    public static void saveApplyJoinClass(Context context, String str) {
//        SharedPreferences sp = context.getSharedPreferences("phone", 1);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(str, true);
//        editor.commit();
//    }

//    public static boolean getApplyJoinClassState(Context context, String str) {
//        SharedPreferences sp = context.getSharedPreferences("phone", 1);
//        boolean hnno = sp.getBoolean(str, false);
//        return hnno;
//    }


//    public static boolean getSumittedWorkstate(Context context, String str) {
//        SharedPreferences sp = context.getSharedPreferences("phone", 1);
//        boolean hnno = sp.getBoolean(str, false);
//        return hnno;
//    }
//
//    public static void saveSumittedWorkstate(Context context, String str, Boolean b) {
//        SharedPreferences sp = context.getSharedPreferences("phone", 1);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(str, b);
//        editor.commit();
//    }
//
//    public static String getSuject(String[] str) {
//        String subject = "";
//        if (str != null && str.length > 0) {
//            for (int i = 0; i < str.length; i++) {
//                subject += ConstantUtils.getSubjectByCode(str[i]) + ",";
//            }
//            if (subject.length() > 0) {
//                subject = subject.substring(0, subject.length() - 1);
//            }
//        }
//        return subject;
//    }
//
//    public static String getType(String type) {
//        String str = "";
//        try {
//            switch (type) {
//                case "1":
//                    str = "[单选]";
//                    break;
//                case "2":
//                    str = "[多选]";
//                    break;
//                case "3":
//                    str = "[判断]";
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return str;
//    }

//    /**
//     * 打开图片查看器
//     *
//     * @param position
//     * @param urls2
//     */
//    public static void imageBrower(int position, ArrayList<String> urls2, Context mContext) {
//        Intent intent = new Intent(mContext, ImagePagerActivity.class);
//        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
//        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
//        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//        mContext.startActivity(intent);
//    }

//    /**
//     * @param singInfo
//     * @return
//     */
//    public static JSONObject toJSONObject(String singInfo) {
//        String str1 = singInfo.replaceAll("\\{|\\}", "");//singInfo是一个map  toString后的字符串。
//        String str2 = str1.replaceAll(" ", "");
//        String str3 = str2.replaceAll(",", "&");
//
//
//        Map<String, String> map = null;
//        if ((null != str3) && (!"".equals(str3.trim()))) {
//            String[] resArray = str3.split("&");
//            if (0 != resArray.length) {
//                map = new HashMap(resArray.length);
//                for (String arrayStr : resArray) {
//                    if ((null != arrayStr) && (!"".equals(arrayStr.trim()))) {
//                        int index = arrayStr.indexOf("=");
//                        if (-1 != index) {
//                            map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
//                        }
//                    }
//                }
//            }
//        }
//        JSONObject jsonObject = JSONObject.fromMap(map);
//        return jsonObject;
//    }


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


}
