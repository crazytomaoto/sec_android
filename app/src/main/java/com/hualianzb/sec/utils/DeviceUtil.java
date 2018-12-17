package com.hualianzb.sec.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.io.InputStream;

/**
 * Created by Administrator on 2018/4/11.
 */

public class DeviceUtil {
    public static void hideKeyBoard(Activity context) {
        if (context != null && context.getCurrentFocus() != null) {
            ((InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(context.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static String readAssert(Context context, String fileName) {
        String jsonString = "";
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    //    //获取手机通讯录数据
//    public static String getData(Context context) {
//        List<String> list = new ArrayList<>();
//        StringBuilder builder = new StringBuilder();
//        //得到ContentResolver对象
//        ContentResolver cr = context.getContentResolver();
//        //取得电话本中开始一项的光标
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        //向下移动光标
//        while (cursor.moveToNext()) {
//            //取得联系人名字
//            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//            String contact = cursor.getString(nameFieldColumnIndex);
//            //取得电话号码
//            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            Cursor phone_cr = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
//
//            while (phone_cr.moveToNext()) {
//                String phoneNumber = phone_cr.getString(phone_cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                contact = contact;
//                ContactBean contactBean = new ContactBean();
//                contactBean.setPhone(phoneNumber);
//                list.add(phoneNumber);
//                builder.append(phoneNumber + ",");
//            }
//            phone_cr.close();
//        }
//        cursor.close();
//        String tel = builder.toString().trim();
//        if (StringUtils.isNotEmpty(tel)) {
//            tel = tel.substring(0, tel.length() - 1);
//            return tel;
//        } else {
//            return "";
//        }
//    }
//
//    public static List<ContactBean> getData(Context context, int type) {
//        List<ContactBean> list = new ArrayList<>();
//        //得到ContentResolver对象
//        ContentResolver cr = context.getContentResolver();
//        //取得电话本中开始一项的光标
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        //向下移动光标
//        while (cursor.moveToNext()) {
//            //取得联系人名字
//            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//            String contact = cursor.getString(nameFieldColumnIndex);
//            //取得电话号码
//            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            Cursor phone_cr = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
//
//            while (phone_cr.moveToNext()) {
//                String PhoneNumber = phone_cr.getString(phone_cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                Object dd = phone_cr.getString(phone_cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
////                格式化手机号
//                PhoneNumber = PhoneNumber.replace("-", "");
////                Bitmap bit = getPhoto(cr, ContactId);
//                contact = contact;
//                ContactBean contactBean = new ContactBean();
//                contactBean.setPhone(PhoneNumber);
//                contactBean.setName(contact);
////                contactBean.setBitmap(bit);
//                list.add(contactBean);
//            }
//            phone_cr.close();
//        }
//        cursor.close();
//        return list;
//    }
    public static void copy(Activity activity, String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("content", content);
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
