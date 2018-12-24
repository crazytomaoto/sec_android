package com.hualianzb.sec.secUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Date:2018/12/12
 * auther:wangtianyun
 * describe:
 */
public class SecUtil {
    private static SecUtil instance = null;

    public SecUtil() {

    }

    public static SecUtil getInstance() {
        if (instance == null) {
            synchronized (SecUtil.class) {
                if (instance == null) {
                    instance = new SecUtil();
                }
            }
        }
        return instance;
    }

    //获取Unix时间戳 单位秒
    public static long getCurrentUnixTimeSecond() {
        long currentUnixTimeSecond =
                Math.round(getCurrentUnixTimeInMillisecond() / 1000);
        return currentUnixTimeSecond;
    }

    //获取Unix时间戳 单位毫秒
    public static long getCurrentUnixTimeInMillisecond() {
        Date date = new Date();
        long currentUnixtime = date.getTime();
        return currentUnixtime;
    }

    //获取Unix时间戳 单位毫秒
    public static String getDatetime() {
        Date date = new Date();
        String Y = date.getYear() + "-";
        String M = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1 + "-");
        String D = date.getDate() + "-";
        String h = date.getHours() + ":";
        String m = date.getMinutes() + ":";
        String s = date.getSeconds() + "";
        String datetime = Y + M + D + h + m + s;
        return datetime;
    }

    public static String getUnixtime(String anyDate) {
        Date date = new Date(anyDate.replace("-", "/"));
        String unixtime = date.getTime() + "";
        return unixtime;
    }

    private static final String strType = "SHA-256";

    /**
     * A small function created as there is a lot of sha256 hashing.
     *
     * @param data
     * @return
     */
    public static byte[] hasha256(final String data) {
        // 是否是有效字符串

        if (data != null && data.length() > 0) {
            try {                // SHA 加密开始				// 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(data.getBytes());
                // 得到 byte 類型结果
                return messageDigest.digest();
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    //    public static String generatePrivateKey() {
//        String privKey = WalletFiles.randomBytes(32).toString("hex");
//
//        return privKey
//
//    }
    private void dsad() {
//        SecP256K1Point secP256K1Point = new SecP256K1Point();
    }
}
