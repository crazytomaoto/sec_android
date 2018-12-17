package com.hysd.android.platform_huanuo.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

/**
 * FileName    : ImageUtils.java
 * Description : 
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-10 下午2:14:37
 **/
public class ImageUtils {

	private static final String TAG = "ImageUtils";

	/** 尝试打开图片次数 **/
	private static final int MAX_TRY_OPEN_IMAGE = 5;

	public static Bitmap getBitmap(String pathFile) {
		return getBitmap(pathFile, /*-1*/1280 * 720);
	}

	/**
	 * 通过路径获取图片，屏蔽掉oom，并且获取图片进行oom重试机制
	 * 
	 * @param pathFile
	 *            pathFile
	 * @param maxLength
	 *            期望大小，（长X宽）
	 * @return Bitmap [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static Bitmap getBitmap(String pathFile, int maxLength) {
		if (BeanUtils.isEmpty(pathFile) || !FileUtils.exists(pathFile)) {
			Logger.e(TAG, "不能获取到bitmap,pathFile=" + pathFile);
			return null;
		}

		Bitmap bitmap = null;
		try {
			BitmapFactory.Options option = new BitmapFactory.Options();

			option.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(pathFile, option);

			option.inJustDecodeBounds = false;

			// 获取压缩值
			option.inSampleSize = computeSampleSize(option, -1, maxLength);

			// 重试次数
			int tryCount = 1;

			Logger.d(TAG, "获取bitmap，pathFile=" + pathFile);

			do {
				if (tryCount > 1) {
					if (option.inSampleSize < 1) {
						option.inSampleSize = 1;
					}

					option.inSampleSize *= tryCount;
				}

				bitmap = getBitmap(pathFile, option);
				// 纠正选择的图片
				int degree = ImageUtils.readPictureDegree(pathFile);
				if (degree % 360 != 0) {
					bitmap = ImageUtils.rotaingImageView(degree, bitmap);
				}
				tryCount++;

				Logger.d(TAG, "尝试打开图片次数，tryCount=" + tryCount + ",压缩大小=" + option.inSampleSize);
			} while (bitmap == null && tryCount < MAX_TRY_OPEN_IMAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static Bitmap getBitmap(String pathFile, BitmapFactory.Options option) {
		Bitmap bitmap = null;
		if (!BeanUtils.isEmpty(pathFile)) {
			InputStream stream = null;
			try {
				stream = new FileInputStream(pathFile);

				bitmap = BitmapFactory.decodeStream(stream, null, option);
			} catch (FileNotFoundException e) {
				Logger.e(TAG, "没有文件，pathFile=" + pathFile, e);
			} catch (OutOfMemoryError oom) {
				long length = -1;
				try {
					length = stream != null ? stream.available() : -1;
				} catch (IOException e) {
					Logger.e(TAG, e.toString(), e);
				}

				Logger.e(TAG, "获取图片内存溢出，option=" + option.inSampleSize + ",length=" + length);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						Logger.e(TAG, "close InputStream is Error", e);
					}
				}
			}
		}

		return bitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 *            angle
	 * @param bitmap
	 *            bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * android源码提供的计算inSampleSize方法
	 * 
	 * @param options
	 *            options
	 * @param minSideLength
	 *            minSideLength
	 * @param maxNumOfPixels
	 *            maxNumOfPixels
	 * @return int [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;

		if (initialSize <= 8) {
			roundedSize = 1;

			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}

		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;

		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {

			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取Uri对应的缩略图
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static Bitmap getThumbnailImage(Context context, Uri uri) {
		Bitmap bitmap = null;
		String[] proj = new String[] { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, proj, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				long thumbnailId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
				Logger.e(TAG, "ThumbnailID = " + thumbnailId);
				// MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
				bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), thumbnailId, Images.Thumbnails.MICRO_KIND, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return bitmap;
	}

	/**
	 * 获取Uri对应的原图路径
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static String getImagePath(Context context, Uri uri) {
		String path = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, proj, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
				Logger.e(TAG, "OrialImagePath = " + path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return path;
	}
}
