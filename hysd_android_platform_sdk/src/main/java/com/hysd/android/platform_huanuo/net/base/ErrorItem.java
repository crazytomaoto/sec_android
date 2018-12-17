package com.hysd.android.platform_huanuo.net.base;

/**
 * FileName    : ErrorItem.java
 * Description : 错误对象
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-9 下午1:01:27
 **/
public class ErrorItem {

	private String errorMessage;
	private String errorCode;

	public ErrorItem(String code, String errors) {
		this.errorCode = code;
		this.errorMessage = errors;
	}

	public ErrorItem(int code, String errors) {
		this.errorCode = String.valueOf(code);
		this.errorMessage = errors;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	@Override
	public String toString() {
		return "errorCode=" + errorCode + " ,errorMessage:" + errorMessage;
	}

}
