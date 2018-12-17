package com.hysd.android.platform_huanuo.net.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.net.base.BaseRequestItem;
import com.hysd.android.platform_huanuo.net.base.IRequestCallBack;
import com.hysd.android.platform_huanuo.net.base.ProtocolType;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType.Encrypt;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType.HttpMethod;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType.RequestType;
import com.hysd.android.platform_huanuo.utils.DateUtils;

/**
 * FileName    : HttpRequestTask.java
 * Description : http的请求单元
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 下午2:24:41
 **/
public class HttpRequest extends BaseRequestItem {

	/**ID 生成器*/
	private static AtomicInteger requestIDs = new AtomicInteger(1);
	/**请求url*/
	private String url;
	/**数据请求的模式*/
	private HttpMethod method = HttpMethod.POST;
	/**请求的参数*/
	protected List<Object> paramValues = new ArrayList<Object>();
	/**参数添加的顺序*/
	protected List<String> paramNames = new ArrayList<String>();
	/**请求的编码*/
	private String encode = "UTF-8";
	/**返回的数据类型*/
	private ResponseDataType dataType = ResponseDataType.JSON;
	/**请求的类型:上传、下载、数据*/
	private RequestType requestType = RequestType.CDATA;
	/**数据保存的地址(暂时用于下载处理)*/
	private String savePath;
	/**超时时间*/
	private int timeout = 60000;
	/**读写时间*/
	private int readtime = 60000;
	/**请求是否加密*/
	private Encrypt requestEncrypt = Encrypt.NONE;
	/**响应是否加密*/
	private Encrypt responseEncrypt = Encrypt.NONE;
	/**添加是否需求进行签名的处理*/
	private boolean sign = true;
	/**网络请求的头信息*/
	private Map<String, String> httpHeaders = new HashMap<String, String>();

	public HttpRequest(IRequestCallBack callback) {
		super(callback);
		this.requestId = String.valueOf(requestIDs.getAndIncrement());

		//默认添加的请求参数(时间戳和，版本号)
		this.addParam("timestamp", DateUtils.getNowDate(DateUtils.TIME_FORMAT));

		// 添加客户端版本号
		String versionName = PlatformConfig.getString("cur_version_name", "1.0.0");
		String versionCode = PlatformConfig.getString("cur_version_code", "1");
		this.addParam("versionName", versionName);
		this.addParam("versionCode", versionCode);
	}

	public HttpRequest(String url, IRequestCallBack callback) {
		this(callback);
		this.url = url;
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public ProtocolType getProtocal() {
		return ProtocolType.HTTP;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpMethod getMethod() {
		return RequestType.UPLOAD == requestType ? HttpMethod.POST : method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public ResponseDataType getDataType() {
		return dataType;
	}

	public void setDataType(ResponseDataType dataType) {
		this.dataType = dataType;
	}

	/**添加基本的参数*/
	public void addParam(String key, Object value) {
		if (value instanceof File) {
			addFileParam(key, (File) value);
		} else {
			this.paramNames.add(key);
			this.paramValues.add(value);
		}
	}

	/**添加文件参数（上传文件的情况）*/
	public void addFileParam(String key, File file) {
		this.paramNames.add(key);
		this.paramValues.add(file);
		this.requestType = RequestType.UPLOAD;
	}

	/**
	 * 添加请求的头域
	 * */
	public void addHttpHeader(String key, String value) {
		httpHeaders.put(key, value);
	}

	/**
	 * 是否设置了头域
	 * @return
	 */
	public boolean hasHttpHeaders() {
		return !httpHeaders.isEmpty();
	}

	/***
	 * 获取请求头
	 * @return
	 */
	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	/**获取上传文件的总大小*/
	public long getFileTotalSize() {
		long totalSize = 0;
		if (requestType == RequestType.UPLOAD) {
			for (Object obj : paramValues) {
				if (obj != null && obj instanceof File) {
					totalSize += ((File) obj).length();
				}
			}
		}
		return totalSize;
	}

	public boolean hasParams() {
		return !paramValues.isEmpty();
	}

	public List<Object> getParamValues() {
		return paramValues;
	}

	public List<String> getParamNames() {
		return paramNames;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getReadtime() {
		return readtime;
	}

	public void setReadtime(int readtime) {
		this.readtime = readtime;
	}

	public Encrypt getRequestEncrypt() {
		return requestEncrypt;
	}

	public void setRequestEncrypt(Encrypt requestEncrypt) {
		this.requestEncrypt = requestEncrypt;
	}

	public Encrypt getResponseEncrypt() {
		return responseEncrypt;
	}

	public void setResponseEncrypt(Encrypt responseEncrypt) {
		this.responseEncrypt = responseEncrypt;
	}

	public boolean isSign() {
		return sign;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}
}
