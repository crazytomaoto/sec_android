package com.hysd.android.platform_huanuo.net.http.image;

import com.hysd.android.platform_huanuo.net.base.IRequestCallBack;
import com.hysd.android.platform_huanuo.net.http.HttpRequest;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType.HttpMethod;

/**
 * FileName    : DownloadRequest.java
 * Description : 下载的统一请求
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : wangtianyun
 * @version    : 1.0
 * Create Date : 2015-12-24 上午10:41:21
 **/
public class DownloadRequest {
	
	private HttpRequest request;
	
	public DownloadRequest(String url,String savePath,IRequestCallBack callback){
		request = new HttpRequest(callback);
		request.setUrl(url);
		request.setSavePath(savePath);
		request.setMethod(HttpMethod.GET);
		request.setDataType(ResponseDataType.FILE);
	}
	
	public void exec(){
		request.send();
	}
	
	public void cancel() {
		request.cancel();
	}
}
