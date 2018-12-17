package com.hualianzb.sec.api;

import com.hysd.android.platform_huanuo.net.base.ErrorItem;
import com.hysd.android.platform_huanuo.net.base.ResultItem;

/**
 * FileName    : BaseResult.java
 * Description : 
 * @Copyright  : GL. All Rights Reserved
 **/
public class CommonResult extends ResultItem {

	public int retcode = 0;
	public String msg;
	public long datetime;
	public ErrorItem error;
	public ProgressInfo progress;

	/***
	 * API业务操作是否成功
	 * @return
	 */
	public boolean isSuccess() {
		return DataConstants.MSG_OK == retcode;
	}

	@Override
	public String toString() {
		return "retCode=" + retcode + " msg=" + msg + " datetime=" + datetime + " error=[" + (error == null ? null : error.toString()) + "] progress=["
				+ (progress == null ? null : progress.toString()) + "]";
	}

}
