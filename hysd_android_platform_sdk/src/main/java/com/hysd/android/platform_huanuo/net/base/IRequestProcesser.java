package com.hysd.android.platform_huanuo.net.base;

import java.util.Collection;

/**
 * FileName    : IRequestProcesser.java
 * Description : 各自协议的请求管理
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 上午10:30:12
 **/
public interface IRequestProcesser {

	/***
	 * 执行的网络请求
	 * @param task
	 * @param callBack
	 */
	public void addRequest(IRequestItem request);

	/**
	 * 取消网络请求
	 * @param task
	 */
	public void cancelRequest(String requestId);
	
	/**
	 * 获取所有管理的任务
	 * @return
	 */
	public Collection<IRequestTask> list();
	
	/**
	 * 请求是否，应用退出时触发
	 */
	public void clear();
}
