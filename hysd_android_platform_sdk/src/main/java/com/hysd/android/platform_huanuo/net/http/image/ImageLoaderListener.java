package com.hysd.android.platform_huanuo.net.http.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * FileName    : ImageLoaderListener.java
 * Description : 图片加载的监听器
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-24 上午10:36:12
 **/
public interface ImageLoaderListener {
	
	/**
	 * 图片加载完成事件
	 * @param operation
	 * @param imageView
	 * @param loadedImage
	 */
	public void onLoadFinish(Object operation,ImageView imageView,Bitmap loadedImage);
}
