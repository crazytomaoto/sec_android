package com.hysd.android.platform_huanuo.net.http;

/**
 * FileName    : ResponseDataType.java
 * Description : 响应的数据类型
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 下午3:20:04
 **/
public enum ResponseDataType {
	/** json数据 */
	JSON,
	/** xml数据 */
	XML,
	/** 文本信息 */
	CONTENT,
	/** 下载文件 */
	FILE;

	/**http请求类型*/
	public enum HttpMethod {
		POST, GET;
	}

	/**请求的类型*/
	public enum RequestType {
		/**上传*/
		UPLOAD,
		/**下载*/
		DOWNLOAD,
		/**基础数据*/
		CDATA;
	}

	/**数据加密的类型*/
	public enum Encrypt {
		/**数据加密类型*/
		NONE,
		/**数据加密类型*/
		GZIP
	}
}
