package com.hysd.android.platform_huanuo.net.http.image;

import java.io.File;
import java.util.UUID;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.net.base.IRequestCallBack;
import com.hysd.android.platform_huanuo.net.base.IResponseItem;
import com.hysd.android.platform_huanuo.net.base.ProtocolType.ResponseEvent;
import com.hysd.android.platform_huanuo.utils.BeanUtils;
import com.hysd.android.platform_huanuo.utils.FileUtils;
import com.hysd.android.platform_huanuo.utils.ImageUtils;
import com.hysd.android.platform_huanuo.utils.Logger;

/**
 * FileName    : ImageLoaderTask.java
 * Description : 执行加载任务
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-24 上午10:41:09
 **/
public class ImageLoaderTask implements Runnable {

	private static final String TAG = "ImageLoaderTask";

	/**tagId*/
	private static final int TAG_ID = 0x7f050018;

	/**是否需要从网络加载*/
	private boolean isNetTask = true;

	private Object operation;
	/**文件管理*/
	private File file;
	/**文件URL*/
	private String url;
	/**默认ID*/
	private int defaultId;
	/**失败ID*/
	private int failId;
	/**view*/
	private ImageView view;
	/**listener*/
	private ImageLoaderListener listener;
	/**任务加载图片的ID,会绑定在ImageView的tag里*/
	private String imageId;

	public ImageLoaderTask(Object operation, File file, ImageView view) {
		this.operation = operation;
		this.file = file;
		this.view = view;
		isNetTask = false;
	}

	public ImageLoaderTask(Object operation, File file, ImageView view, ImageLoaderListener listener) {
		this.operation = operation;
		this.file = file;
		this.view = view;
		this.listener = listener;
		isNetTask = false;
	}

	public ImageLoaderTask(Object operation, File file, ImageView imageView, int defaultId, int failId) {
		this.operation = operation;
		this.file = file;
		this.view = imageView;
		this.defaultId = defaultId;
		this.failId = failId;
		isNetTask = false;
	}

	public ImageLoaderTask(Object operation, File file, ImageView imageView, int defaultId, int failId, ImageLoaderListener listener) {
		this.operation = operation;
		this.file = file;
		this.view = imageView;
		this.defaultId = defaultId;
		this.failId = failId;
		this.listener = listener;
		isNetTask = false;
	}

	public ImageLoaderTask(Object operation, String url, ImageView view) {
		this.operation = operation;
		this.url = url;
		this.view = view;
	}

	public ImageLoaderTask(Object operation, String url, ImageView view, ImageLoaderListener listener) {
		this.operation = operation;
		this.url = url;
		this.view = view;
		this.listener = listener;
	}

	public ImageLoaderTask(Object operation, String url, ImageView imageView, int defaultId, int failId) {
		this.operation = operation;
		this.url = url;
		this.view = imageView;
		this.defaultId = defaultId;
		this.failId = failId;
	}

	public ImageLoaderTask(Object operation, String url, ImageView imageView, int defaultId, int failId, ImageLoaderListener listener) {
		this.operation = operation;
		this.url = url;
		this.view = imageView;
		this.defaultId = defaultId;
		this.failId = failId;
		this.listener = listener;
	}

	/***
	 * 判断下载的URL是否缓存过
	 * @param url
	 */
	protected void check() {
		//设置image的唯一tag
		try{
			if (view != null) {
				imageId = UUID.randomUUID().toString();
				view.setTag(TAG_ID, imageId);
			}
		}catch(Exception e){
			e.printStackTrace();
			imageId = null;
			Logger.e(TAG, "view.setTag error"+e.getMessage());
		}
		//确认是否有文件缓存
		if (!TextUtils.isEmpty(url)) {
			String cachePath = ImageLoaderManager.getIntance().getCachePath();
			if (TextUtils.isEmpty(cachePath) || !FileUtils.exists(cachePath)) {
				if (view != null) {
					cachePath = FileUtils.getContextPath(view.getContext(), "/image/");
					FileUtils.checkExists(cachePath);
				} else {
					throw new RuntimeException("not save Path");
				}
			}
			cachePath = cachePath + BeanUtils.md532(url);
			//判断缓存地址是否存在
			if (FileUtils.exists(cachePath)) {
				this.file = new File(cachePath);
				isNetTask = false;
			} else {
				this.file = new File(cachePath);
				isNetTask = true;
			}
		}else{
			if(file == null){
				throw new RuntimeException("not set path and no URL");
			}
		}
		
		
		//需要从网络加载
		if (defaultId > 0 && isNetTask) {
			if (this.view != null) {
				try {
					view.getHandler().post(new Runnable() {

						@Override
						public void run() {
							view.setImageResource(defaultId);
						}
					});
				} catch (Exception e) {
					Logger.e(TAG, "setImageView defaultId " + e.getMessage());
				}
			} else {
				Logger.e(TAG, "can't update defaultId image to ui ");
			}
		}
	}

	@Override
	public void run() {
		Logger.d(TAG, "run image Task:" + this.url + " isNetTask:" + isNetTask);
		if (!isNetTask) {
			setImageView(ImageUtils.getBitmap(this.file.getAbsolutePath()));
		} else {
			DownloadRequest request = new DownloadRequest(url, this.file.getAbsolutePath(), new IRequestCallBack() {

				@Override
				public void onResponseEvent(ResponseEvent event, IResponseItem response) {
					if (ResponseEvent.isFinish(event)) {
						setImageView(ImageUtils.getBitmap(file.getAbsolutePath()));
					}
				}
			});
			request.exec();
		}
	}

	/**
	 * 更新图片
	 * @param bitmap
	 */
	protected void setImageView(final Bitmap bitmap) {
		if (this.view != null) {
			//监测ID是否被替换过
			String oldImageId = (String) view.getTag(TAG_ID);
			if (TextUtils.isEmpty(imageId) || (!TextUtils.isEmpty(imageId) && imageId.equals(oldImageId))) {
				try {
					PlatformConfig.getHandler().post(new Runnable() {

						@Override
						public void run() {
							if (listener == null) {
								if (bitmap != null) {
									view.setImageBitmap(bitmap);
								} else {
									if (failId > 0) {
										view.setImageResource(failId);
									}
								}
							} else {
								listener.onLoadFinish(operation, view, bitmap);
							}
						}
					});
				} catch (Exception e) {
					Logger.e(TAG, "setImageView error " + e.getMessage());
				}
			} else {
				Logger.d(TAG, "image id has change,not do setView");
			}
		} else {
			Logger.e(TAG, "can't update image to ui ");
		}
		//通知任务完成
		if (isNetTask) {
			ImageLoaderManager.getIntance().finishTask(this);
		}
	}

	public boolean isNetTask() {
		return isNetTask;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDefaultId() {
		return defaultId;
	}

	public void setDefaultId(int defaultId) {
		this.defaultId = defaultId;
	}

	public int getFailId() {
		return failId;
	}

	public void setFailId(int failId) {
		this.failId = failId;
	}

	public ImageView getView() {
		return view;
	}

	public void setView(ImageView view) {
		this.view = view;
	}

	public ImageLoaderListener getListener() {
		return listener;
	}

	public void setListener(ImageLoaderListener listener) {
		this.listener = listener;
	}
}
