package com.hysd.android.platform_huanuo.net.base;

/**
 * FileName    : BaseResponse.java
 * Description : 响应的基类
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-9 下午1:06:19
 **/
public abstract class BaseResponseItem implements IResponseItem {

	/**错误数据*/
	protected ErrorItem error;
	/**返回的数据*/
	protected Object resultData;
	/**返回的包体大小*/
	protected long contentSize;
	/**进度计算的包体总长度*/
	protected long totalSize;
	/**进度计算的包体总长度*/
	protected long completeSize;
	/**包头数据*/
	protected ResultItem headerItem;

	@Override
	public ErrorItem getErrorItem() {
		return error;
	}

	@Override
	public void setError(String code, String error) {
		this.error = new ErrorItem(code, error);
	}

	@Override
	public void setResultData(Object obj) {
		this.resultData = obj;
	}

	@Override
	public void setContentSize(long size) {
		this.contentSize = size;
	}

	@Override
	public long getContentSize() {
		return contentSize;
	}

	@Override
	public void setTotalSize(long size) {
		this.totalSize = size;
	}

	@Override
	public long getTotalSize() {
		return totalSize;
	}

	@Override
	public void setCompleteSize(long size) {
		this.completeSize = size;
	}

	@Override
	public long getCompleteSize() {
		return completeSize;
	}

	@Override
	public void setHeaderData(ResultItem item) {
		this.headerItem = item;
	}

	@Override
	public ResultItem getHeaderData() {
		return headerItem;
	}
	
}
