package com.hualianzb.sec.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wty on 2017/8/22.
 */
public class ImageUtils {

    /**
     * 从网络读取图片
     *
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(File saveFile, String imageUrl) {
        InputStream in = null;
        OutputStream out = null;
        try {
            URL url = new URL(imageUrl);

            URLConnection connection = url.openConnection();

            connection.setConnectTimeout(30 * 1000);

            out = new FileOutputStream(saveFile);

            in = connection.getInputStream();

            byte[] buffer = new byte[1024];

            int len = -1;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            //return BitmapFactory.decodeFile(saveFile.getPath());
            return decodeFile(saveFile.getPath(), 100, 100);

            //Bitmap bitmap = decodeFile(saveFile.getPath(), 50, 50);
            //return zoomBitmap(bitmap,30,30);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 从网络断获取图片
     *
     * @param urlpath
     * @return
     */
    public static Bitmap getBitMBitmap(String urlpath) {
        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
            // TODO Auto-generated catch block
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * 图片处理，使用图片二次采样
     *
     * @param fileName
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap decodeFile(String fileName, int newWidth, int newHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();

        // 设置此选项，则decodeFile并不先分配空间
        options.inJustDecodeBounds = true;

        // 先不分配空间处理解码，主要就是获取图片宽高信息等
        BitmapFactory.decodeFile(fileName, options);

        // RGB_565 - 4.2 -> ARGB_B8888

        // 设置图片样式主要作用: 设置单位像素占用的字节数

        // RGB_565 - 2个字节 无透明度
        // ARGB_8888 - 4个字节
        // ALPHA_8 - 一个字节 没有alpha值，并且没有RGB值
        // ARGB_4444 - 2个字节 此格式成像效果比较差
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        // 获取图片的大小
        // 获取图片的宽 200
        int imageWidth = options.outWidth;

        // 获取图片的高 200
        int imageHeight = options.outHeight;

        // 获取原有图片宽高和新图片的宽高的比例
        float ratioWidth = (float) imageWidth / newWidth;
        float ratioHeight = (float) imageHeight / newHeight;

        // 获取处理的比例
        int inSampleSize = (int) (ratioWidth > ratioHeight ? ratioHeight : ratioWidth);

        //options.inSampleSize = inSampleSize;

        // 分配空间处理解码
        options.inJustDecodeBounds = false;

        // 解码文件
        Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

        return bitmap;
    }

    /**
     * 使用Matrix矩阵进行缩放
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private static Bitmap zoomBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();

        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        int widthScale = newWidth / imageWidth;
        int heightScale = newHeight / imageHeight;

        matrix.postScale(widthScale, heightScale);

        Log.i("Matrix", "zoomBitmap = " + imageWidth + ", " + imageHeight);

        Bitmap newBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, imageWidth, imageHeight, matrix, true);

        return newBitmap;
    }


    //获取等级图片
    public static int getWalletImage(int imgIndex) {

        return DrawableRes.walletImg[(imgIndex)];
    }

}








