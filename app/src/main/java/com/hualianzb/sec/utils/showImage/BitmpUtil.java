package com.hualianzb.sec.utils.showImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BitmpUtil {
	
	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	
	
	//获得当前外部储存设备的目录
	public static boolean isDirExist(String dir){
	File file = new File(dir);
		if(!file.exists()) {
			return false;
		} 
		return true;
	}
	
	
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        return drawableToBitmap(drawable,width,height);
    }
    
    public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
//      int width = drawable.getIntrinsicWidth();
//      int height = drawable.getIntrinsicHeight();
      Bitmap bitmap = Bitmap.createBitmap(width, height,
      drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
      : Bitmap.Config.RGB_565);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, width, height);
      drawable.draw(canvas);
      return bitmap;
  }	  
    
    /**
	* 加载本地图片
	* http://bbs.3gstdy.com
	* @param url
	* @return
	*/
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	
		  
    /**
     * 按正方形裁切图片
     */
    public static Bitmap squareImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }
    
    public static Bitmap threeFourImageCropByUrl(String url) {
    	Bitmap bitmap = null;
		try {
			bitmap = revitionImageSize(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return threeFourImageCrop(bitmap);
    }
    
    public static Bitmap squreImageCropByUrl(String url) {
    	Bitmap bitmap = null;
		try {
			bitmap = revitionImageSize(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return squreImageCrop(bitmap);
    }
    
    public static Bitmap threeFourImageCrop(Bitmap bitmap) {
       return CutOutImageCropByRate(bitmap,3.0/4.0);
    }
    
    public static Bitmap squreImageCrop(Bitmap bitmap) {
        return CutOutImageCropByRate(bitmap,1.0);
     }
    //rate的比例是：高/宽
    public static Bitmap CutOutImageCropByRate(Bitmap bitmap, double rate) {
        double w = bitmap.getWidth(); // 得到图片的宽，高
        double h = bitmap.getHeight();
        
        double proportion = h/w;
        double cutout_w = 0,cutout_h = 0;
        double retX = 0,retY = 0;
        
        if(proportion == rate){
        	return bitmap;
        }
        if(proportion < rate){
        	cutout_w = h/rate;
    		cutout_h = h;
        	retX = (w-cutout_w)/2.0;
        	retY = 0;
        }
        if(proportion>rate){
        	cutout_h = w*rate;
        	cutout_w = w;
        	retX = 0;
        	retY = (h-cutout_h)/2.0;
        }
        
        //下面这句是关键
        return Bitmap.createBitmap(bitmap, (int)retX, (int)retY, (int)cutout_w, (int)cutout_h, null, false);
    }

	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

}
