package com.hysd.android.platform_huanuo.net.base;

import java.util.List;

/**
 * FileName    : IResponseItem.java
 * Description : 请求返回的接口
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 下午5:00:06
 **/
public interface IResponseItem {

	/**
	 * 获取指定的结果接
	 * @param T
	 * @return
	 */
	public <T> T getResultItem(Class<T> T);

	/***
	 * 获取指定的结果集
	 * @param T
	 * @return
	 */
	public <T> List<T> getResultArray(Class<T> T);

	/***
	 * 设置数据
	 * @param obj
	 */
	public void setResultData(Object obj);

	/**
	 * 响应的中长度
	 * @param size
	 */
	public void setContentSize(long size);

	/***
	 * 获取响应的长度
	 * @return
	 */
	public long getContentSize();

	/**
	 * 计算进度的总长度
	 * @param size
	 */
	public void setTotalSize(long size);

	/***
	 * 获取进度的总长度
	 * @return
	 */
	public long getTotalSize();

	/**
	 * 计算进度的完成大小
	 * @param size
	 */
	public void setCompleteSize(long size);

	/***
	 * 获取进度的完成大小
	 * @return
	 */
	public long getCompleteSize();

	/**
	 * 返回错误对象
	 * @return
	 */
	public ErrorItem getErrorItem();

	/**
	 * 设置code信息
	 * @param code
	 * @param error
	 */
	public void setError(String code, String error);

	/**
	 * 设置头域数据
	 * @param item
	 */
	public void setHeaderData(ResultItem item);

	/**
	 * 获取响应头信息
	 * @return
	 */
	public ResultItem getHeaderData();

}
