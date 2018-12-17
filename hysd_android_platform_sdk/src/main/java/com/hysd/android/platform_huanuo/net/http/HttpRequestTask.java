package com.hysd.android.platform_huanuo.net.http;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

import android.text.TextUtils;

import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.net.base.BaseRequestTask;
import com.hysd.android.platform_huanuo.net.base.IRequestItem;
import com.hysd.android.platform_huanuo.net.base.IResponseItem;
import com.hysd.android.platform_huanuo.net.base.ResultItem;
import com.hysd.android.platform_huanuo.net.base.ProtocolType.ResponseEvent;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType.Encrypt;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType.HttpMethod;
import com.hysd.android.platform_huanuo.net.http.ResponseDataType.RequestType;
import com.hysd.android.platform_huanuo.utils.BeanUtils;
import com.hysd.android.platform_huanuo.utils.FileUtils;
import com.hysd.android.platform_huanuo.utils.Logger;

/**
 * FileName    : HttpConnection.java
 * Description : http请求管理
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-9 上午9:37:05
 **/
public class HttpRequestTask extends BaseRequestTask {

	private static final String TAG = "HttpRequestTask";
	/**缓存区大小*/
	protected static int DEFAULT_BUFFER_SIZE = 8192;
	/**http请求成功code*/
	protected static int SUCCESS = 200;
	/**当前的httpConnection*/
	private HttpURLConnection conn = null;
	/**表单请求的分隔符*/
	private String boundary = "-----------------------------7da2e536604c8";

	public HttpRequestTask() {

	}

	@Override
	public void init(IRequestItem request) {
		super.request = request;
		this.response = new HttpResponse();
	}

	/**
	 * 处理httpRequest
	 * @param request
	 */
	@Override
	public void exec() {
		InputStream is = null;
		OutputStream os = null;
		try {
			HttpRequest request = (HttpRequest) this.request;
			//请求开始
			doCallBack(ResponseEvent.START);

			if (HttpMethod.POST == request.getMethod()) {
				if (RequestType.UPLOAD == request.getRequestType()) {
					doForm(os, request);
				} else {
					doPost(os, request);
				}
			} else {
				doGet(os, request);
			}

			Logger.i(TAG, "onRequest requestId " + request.getRequestId() + " " + request.getUrl());
			// 获取返回的responseCode
			int responseCode = conn.getResponseCode();
			Logger.i(TAG, "onResponse requestId " + request.getRequestId() + " code:" + responseCode);

			// 根据返回的responseCode进行处理
			if (responseCode == SUCCESS) {
				//总的响应大小
				response.setContentSize(conn.getContentLength());
				//处理包头响应数据
				processHeaderData(conn.getHeaderFields());
				Logger.i(TAG, "onResponse requestId " + request.getRequestId() + " totalSize:" + response.getContentSize());

				//响应的流
				is = conn.getInputStream();
				//数据流进行重构或者切换
				buildGzipInputStream(conn, request, conn.getInputStream());
				//数据统一处理
				processResponseData(is, request, response);
				//请求成功
				doCallBack(ResponseEvent.SUCCESS);
			} else {
				// 错误信息
				Logger.d(TAG, "requestId " + request.getRequestId() + " event:" + ResponseEvent.ERROR + " http connection error:" + responseCode);
				response.setError(String.valueOf(responseCode), "http connection error:" + responseCode);
				doCallBack(ResponseEvent.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setError("", "http connection error:" + e.getMessage());
			doCallBack(ResponseEvent.ERROR);
		} finally {
			BeanUtils.close(os);
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * 做post请求
	 * @param os
	 * @param request
	 */
	protected void doPost(OutputStream os, HttpRequest request) {
		try {
			// 创建http连接
			conn = getConnection(request, request.getUrl());
			// 生成请求体（补充连接的头信息）
			byte[] body = buildRequestBody(request);
			// 进行连接
			conn.connect();

			if (body != null) {
				// 获取请求输出对象,提交请求的对应的参数
				os = new BufferedOutputStream(conn.getOutputStream(), DEFAULT_BUFFER_SIZE);
				// 重新创建新的数据输出方式（压缩或者其他的）
				buidOutputStream(request, os);
				// 请求内容输入
				os.write(body);
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			Logger.w(TAG, "requestId doPost error:", e);
		}
	}

	/**
	 * 做get请求
	 * @param os
	 * @param request
	 */
	protected void doGet(OutputStream os, HttpRequest request) {
		try {
			if (request.isSign()) {
				BeanUtils.buildSign(request);
			}
			// 创建http连接
			conn = getConnection(request, buildURL(request));
			// 进行连接
			conn.connect();
		} catch (Exception e) {
			Logger.w(TAG, "requestId doPost error:", e);
		}
	}

	/**
	 * 做表单提交
	 * @param os
	 * @param request
	 */
	protected void doForm(OutputStream os, HttpRequest request) {
		try {
			// 创建http连接
			conn = getConnection(request, request.getUrl());
			// 进行连接
			conn.connect();
			os = new BufferedOutputStream(conn.getOutputStream(), DEFAULT_BUFFER_SIZE);
			//创建表单内容
			buildFormBody(request, os);
			//添加表单信息
			StringBuffer endMsg = new StringBuffer();
			endMsg.append("--" + boundary + "--" + "\r\n");
			endMsg.append("\r\n");
			os.write(endMsg.toString().getBytes());
			os.flush();
		} catch (Exception e) {
			Logger.w(TAG, "requestId doPost error:", e);
		}
	}

	/**
	 * 获取HttpURLConnection 包括 http 和 https
	 * 
	 * @param request
	 * @return
	 */
	public HttpURLConnection getConnection(HttpRequest request, String url) {
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			if (isHttps(realUrl)) {
				conn = getHttpsConnection(realUrl);
			} else {
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
			conn.setInstanceFollowRedirects(true);
			// 超时时间
			conn.setReadTimeout(request.getReadtime());
			conn.setConnectTimeout(request.getTimeout());
			// Read from the connection. Default is true
			conn.setDoInput(true);// 发送输入流
			// http正文内，因此需要设为true
			if (HttpMethod.POST == request.getMethod()) {
				conn.setDoOutput(true);// 发送POST请求必须设置允许输出
			}
			// Post cannot use caches
			// Post 请求不能使用缓存
			conn.setUseCaches(false);
			// Set the post method. Default is GET
			conn.setRequestMethod(request.getMethod().name());
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Charset", request.getEncode()); //编码
			// conn.setRequestProperty("Content-Length",
			// String.valueOf(request.length));
			// conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			//请求的公用参数,请求前实时去获取最新的token,防止时间间隔造成token不是最新的
			Logger.i(TAG, "USERTOKEN = " + PlatformConfig.getString(PlatformConfig.USER_TOKEN));
			if (!BeanUtils.isEmpty(PlatformConfig.getString(PlatformConfig.USER_TOKEN))) {
				request.addHttpHeader(PlatformConfig.USER_TOKEN, PlatformConfig.getString(PlatformConfig.USER_TOKEN));
			}
			//添加请求自定义的头域
			if (request.hasHttpHeaders()) {
				for (Entry<String, String> header : request.getHttpHeaders().entrySet()) {
					conn.setRequestProperty(header.getKey(), header.getValue());
					Logger.d(TAG, "header key:"+header.getKey()+ " value:" + header.getValue());
				}
			}
			if (request.getRequestType() == RequestType.UPLOAD) {
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			}
			//添加签名的处理 post
			if (request.isSign() && HttpMethod.POST == request.getMethod()) {
				BeanUtils.buildSign(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.w(TAG, "getConnection error:", e);
		}
		return conn;
	}

	/**
	 * 是否是https请求
	 * 
	 * @param url
	 * @return
	 */
	protected boolean isHttps(URL url) {
		return url.getProtocol().toLowerCase(Locale.US).equals("https");
	}

	/**
	 * 创建输出流
	 * 
	 * @param request
	 * @param os
	 */
	protected void buidOutputStream(HttpRequest request, OutputStream os) {
		if (Encrypt.GZIP == request.getRequestEncrypt()) {
			try {
				os = new GZIPOutputStream(os);
			} catch (IOException e) {
				e.printStackTrace();
				Logger.e(TAG, "buidOutputStream GZIP error:", e);
			}
		}
	}

	/**
	 * 创建输入流
	 * @param connection
	 * @param request
	 * @param os
	 */
	protected void buildGzipInputStream(HttpURLConnection connection, HttpRequest request, InputStream ins) {
		try {
			if ("gzip".equalsIgnoreCase(connection.getContentEncoding()) || Encrypt.GZIP == request.getResponseEncrypt()) {
				ins = new GZIPInputStream(ins);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Logger.e(TAG, "buildGzipInputStream GZIP error:", e);
		}
	}

	/**
	 * 创建参数body
	 * 
	 * @param request
	 * @return
	 */
	protected String buildURL(HttpRequest request) {
		StringBuffer data = new StringBuffer(request.getUrl());
		if (request.hasParams()) {
			try {
				int length = request.getParamNames().size();
				StringBuilder params = new StringBuilder();
				for (int i = 0; i < length; i++) {
					Object value = request.getParamValues().get(i);
					params.append(request.getParamNames().get(i));
					params.append("=");
					params.append(value == null ? "" : (URLEncoder.encode(value.toString(), request.getEncode())));
					if (i != length - 1) {
						params.append("&");
					}
				}
				String appendChar = request.getUrl().indexOf("?") > 0 ? "" : "?";
				data.append(appendChar + params.toString());
				Logger.d(TAG, "doget url:" + data);
			} catch (Exception e) {
				Logger.e(TAG, "buildURL error:", e);
			}

		}
		return data.toString();
	}

	/**
	 * 创建参数body
	 * 
	 * @param request
	 * @return
	 */
	protected byte[] buildRequestBody(HttpRequest request) {
		byte[] data = null;
		if (request.hasParams()) {
			try {
				int length = request.getParamNames().size();
				StringBuilder params = new StringBuilder();
				for (int i = 0; i < length; i++) {
					Object value = request.getParamValues().get(i);
					params.append(request.getParamNames().get(i));
					params.append("=");
					params.append(value == null ? "" : (URLEncoder.encode(value.toString(), request.getEncode())));
					if (i != length - 1) {
						params.append("&");
					}
				}
				Logger.d(TAG, request.getRequestId() + " params " + params.toString());
				data = params.toString().getBytes(request.getEncode());
			} catch (Exception e) {
				Logger.e(TAG, "buildRequestBody error:", e);
			}

		}
		return data;
	}

	/**
	 * 创建表单请求
	 * @param request
	 * @param outStream
	 */
	protected void buildFormBody(HttpRequest request, OutputStream outStream) {
		if (request.hasParams()) {
			try {
				//总数
				response.setTotalSize(request.getFileTotalSize());
				int length = request.getParamNames().size();
				for (int i = 0; i < length; i++) {
					String name = request.getParamNames().get(i);
					Object value = request.getParamValues().get(i);
					if (value instanceof File) {
						writeFileParams(name, (File) value, request.getEncode(), outStream);
					} else {
						writeStringParams(name, value == null ? "" : value.toString(), request.getEncode(), outStream);
					}
				}
			} catch (Exception e) {
				Logger.e(TAG, "buildFormBody error:", e);
			}

		}
	}

	/**
	 * 基础参数
	 * @param name
	 * @param value
	 * @param chartset
	 * @param outStream
	 * @throws Exception
	 */
	protected void writeStringParams(String name, String value, String chartset, OutputStream outStream) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("--" + boundary + "\r\n");
		buffer.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
		buffer.append("\r\n");
		buffer.append(URLEncoder.encode(value.toString(), chartset) + "\r\n");
		outStream.write(buffer.toString().getBytes());
	}

	/**
	 * 写文件
	 * @param name
	 * @param file
	 * @param chartset
	 * @param outStream
	 * @throws Exception
	 */
	protected void writeFileParams(String name, File file, String chartset, OutputStream outStream) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("--" + boundary + "\r\n");
		buffer.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + URLEncoder.encode(file.getName(), chartset) + "\"\r\n");

		// 添加Content-Type
		String contentType = FileUtils.getFileMIME(file.getName());
		if (contentType == null || contentType.length() == 0) {
			contentType = "application/octet-stream";
		}

		buffer.append("Content-Type: " + contentType + "\r\n");
		buffer.append("\r\n");
		outStream.write(buffer.toString().getBytes());
		uploadFile(file, outStream);
		outStream.write("\r\n".getBytes());
	}

	/***
	 * 获取https连接
	 * 
	 * @param url
	 * @return
	 */
	protected HttpsURLConnection getHttpsConnection(URL url) {
		HttpsURLConnection https = null;
		try {
			// 采用X509的证书信息机制SSLContext
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			https = (HttpsURLConnection) url.openConnection();

			// 域名主机验证
			https.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Logger.e(TAG, "getHttpsConnection error:", e);
		}
		return https;
	}

	/**
	 * 采用X509的证书信息机制
	 * 
	 * @author 刘剑
	 * 
	 */
	private class MyTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}

	/**
	 * 解析包头的数据
	 */
	protected void processHeaderData(Map<String, List<String>> headers) {
		try {
			if (headers != null) {
				ResultItem header = new ResultItem();
				for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
					String key = entry.getKey();
					List<String> values = entry.getValue();
					if (!TextUtils.isEmpty(key)) {
						header.put(entry.getKey(), (values != null && values.size() == 1) ? values.get(0) : values);
						Logger.d(TAG, "header key:" + entry.getKey() + " value:" + header.getString(entry.getKey()));
					}
				}
				response.setHeaderData(header);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.e(TAG, "processHeaderData error:", e);
		}
	}

	/**
	 * 处理服务器响应对象
	 * @param responseData
	 * @param request
	 * @param response
	 */
	protected void processResponseData(Object responseData, IRequestItem request, IResponseItem response) {
		if (responseData != null) {
			InputStream stream = null;
			HttpRequest httpRequest = (HttpRequest) request;
			if ((responseData instanceof InputStream)) {
				stream = (InputStream) responseData;
			} else if ((responseData instanceof String)) {
				stream = new ByteArrayInputStream(((String) responseData).getBytes());
			}

			if (stream != null) {
				Object result = null;
				try {
					if (httpRequest.getDataType() == ResponseDataType.JSON)
						result = processJson(response, httpRequest, stream);
					else if (httpRequest.getDataType() == ResponseDataType.CONTENT) {
						result = processContent(httpRequest, stream);
						Logger.d(TAG, "onResponse requestId " + request.getRequestId() + " result:" + result);
					} else if (httpRequest.getDataType() == ResponseDataType.FILE) {
						processFile(response, stream, httpRequest);
						result = httpRequest.getSavePath();

					} else if (httpRequest.getDataType() == ResponseDataType.XML) {
						// result = processXml(httpRequest, stream);
					}
					response.setResultData(result);
				} catch (Exception e) {
					e.printStackTrace();
					Logger.e(TAG, "processResponseData error:", e);
				}
			}
		}
	}

	/**
	 * 直接返回字符串
	 * @param httpRequest
	 * @param dataSoure
	 * @return
	 */
	protected String processContent(HttpRequest httpRequest, InputStream dataSoure) {
		return FileUtils.getContent(dataSoure, httpRequest.getEncode());
	}

	/**
	 * 处理文件
	 * @param stream
	 * @param request
	 */
	protected void processFile(IResponseItem response, InputStream stream, HttpRequest httpRequest) {
		if (ResponseDataType.FILE == httpRequest.getDataType()) {
			if (!BeanUtils.isEmpty(httpRequest.getSavePath())) {
				//总数
				response.setTotalSize(response.getContentSize());
				downloadFile(stream, httpRequest.getSavePath());
			} else {
				response.setError("1004", "not set save path");
			}
		}
	}

	/**
	 * 
	 * @param response
	 * @param httpRequest
	 * @param dataSoure
	 * @return
	 */
	protected ResultItem processJson(IResponseItem response, HttpRequest httpRequest, InputStream dataSoure) {
		ResultItem item = new ResultItem();
		try {
			String jsonContent = FileUtils.getContent(dataSoure, httpRequest.getEncode());
			Logger.d(TAG, "onResponse requestId " + httpRequest.getRequestId() + " result:" + jsonContent);
			JSONObject json = new JSONObject(jsonContent);
			item = new ResultItem(json);
			item.setJsonContent(jsonContent);
		} catch (Exception e) {
			e.printStackTrace();
			response.setError("1023", "processJson error:" + e.getMessage());
		} finally {
			BeanUtils.close(dataSoure);
		}
		return item;
	}

	/**
	 * 获取文件内容
	 * @param file
	 * @return
	 * @throws Exception
	 */
	protected void uploadFile(File file, OutputStream out) throws Exception {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] b = new byte[DEFAULT_BUFFER_SIZE / 2];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
				//上报进度
				response.setCompleteSize(response.getCompleteSize() + n);
				doCallBack(ResponseEvent.PROGRESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BeanUtils.close(in);
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param inps
	 * @param filePath
	 */
	protected void downloadFile(InputStream inps, String filePath) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			int len = 0;
			byte[] data = new byte[DEFAULT_BUFFER_SIZE / 2];
			while ((len = inps.read(data)) != -1) {
				out.write(data, 0, len);
				//上报进度
				response.setCompleteSize(response.getCompleteSize() + len);
				doCallBack(ResponseEvent.PROGRESS);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BeanUtils.close(inps);
			BeanUtils.close(out);
		}
	}

	@Override
	public void cancel() {
		//上报取消时间
		doCallBack(ResponseEvent.CANCEL);
		try {
			BeanUtils.close(conn.getOutputStream());
			BeanUtils.close(conn.getInputStream());
			conn.disconnect();
		} catch (Exception e) {
			//			e.printStackTrace();
		}
	}
}
