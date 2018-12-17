package com.hysd.android.platform_huanuo.net.http;

import com.hysd.android.platform_huanuo.net.base.IRequestItem;

/**
 * FileName    : IRequestCallBack.java
 * Description : 网络请求的callBack
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 上午10:49:51
 **/
public interface IHttpRequestCallBack {
	/**
	 * 任务开始运行
	 * @param item
	 */
	public void onStart(IRequestItem item);

	/**
	 * 任务上报进度
	 * @param item
	 */
	public void onProgress(IRequestItem item);

	/**
	 * 任务出错
	 * @param item
	 */
	public void onError(IRequestItem item);

	/**
	 * 任务完成
	 * @param item
	 */
	public void onFinish(IRequestItem item);

	/**
	 * 任务被取消
	 * @param item
	 */
	public void onCancel(IRequestItem item);
}
