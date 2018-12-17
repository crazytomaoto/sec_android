package com.hysd.android.platform_huanuo.net.base;



/**
 * FileName    : IRequestTask.java
 * Description : 真正做请求的任务
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-25 下午5:54:05
 **/
public interface IRequestTask {
	
	/**
	 * 执行请求
	 * @param request
	 */
	public void init(IRequestItem request);
	
	/**
	 * 执行
	 */
	public void exec();
	
	/**
	 * 取消
	 */
	public void cancel();
}
