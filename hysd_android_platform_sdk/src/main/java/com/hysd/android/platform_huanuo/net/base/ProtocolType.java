package com.hysd.android.platform_huanuo.net.base;

/**
 * FileName    : ProtocolType.java
 * Description : 网络请求的协议类型
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 上午10:28:33
 **/
public enum ProtocolType {
	/**http 和 https的网络协议*/
	HTTP,
	/**webServic协议*/
	WEBSERVICES,
	/**socket协议*/
	SOCKET;

	/**请求响应的事件*/
	public enum ResponseEvent {
		/**任务开始*/
		START,
		/**任务上报进度*/
		PROGRESS,
		/**任务完成*/
		SUCCESS,
		/**任务出错*/
		ERROR,
		/**请求取消*/
		CANCEL;
		
		public static boolean isFinish(ResponseEvent event){
			return event == ResponseEvent.SUCCESS || event == ResponseEvent.ERROR|| event == ResponseEvent.CANCEL;
		}
	}
}
