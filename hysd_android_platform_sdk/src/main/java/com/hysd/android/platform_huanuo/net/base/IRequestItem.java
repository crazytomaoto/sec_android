package com.hysd.android.platform_huanuo.net.base;

/**
 * FileName    : IRequestTask.java
 * Description : 
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 上午10:29:39
 **/
public interface IRequestItem {
	/***
	 * 请求是否有效
	 * @return
	 */
	public boolean isValid();

	/***
	 * 获取网络协议
	 * @return
	 */
	public ProtocolType getProtocal();

	/***
	 * 获取请求ID
	 * @return
	 */
	public String getRequestId();

	/**
	 * 获取请求的状态
	 * @return
	 */
	public RequestStatus getStatus();

	/***
	 * 设置请求的状态
	 * @param status
	 */
	public void setRequestStatus(RequestStatus status);

	/**
	 * 获取对应callback
	 * @return
	 */
	public IRequestCallBack getCallback();

	/**请求发送*/
	public void send();
	
	/**取消请求*/
	public void cancel();

	/**请求任务的机制状态*/
	public enum RequestStatus {
		/**未执行*/
		NONE,
		/**执行中*/
		RUNNING,
		/**执行失败*/
		ERROR,
		/**请求取消*/
		CANCEL,
		/**请求成功*/
		SUCCESS;
	}
}
