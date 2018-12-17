package com.hualianzb.sec.api;

/**
 * FileName    : DataConstants.java
 * Description : 数据常量 和 枚举
 * @Copyright  : GL. All Rights Reserved
 * @Company    :  
 * @author     : 王天运
 * @version    : 1.0
 * Create Date : 2014-4-14 下午3:50:50
 **/
public class DataConstants {

	public static final String YES = "yes";

	public static final String NO = "no";

	/**api操作成功的接口*/
	public static final int MSG_OK = 200;
	/**api token键值*/
	public static final String USER_TOKEN = "USER_TOKEN";

	/**
	 * 全局统一ErrorCode
	 */
	public static interface GlobalErrorCode {

		/** 用户未登陆 */
		public static final String USER_NOT_LOGIN = "100001";

		/** 用户Token过期 */
		public static final String USER_TOKEN_EXPIRE = "100002";
	}

	

}