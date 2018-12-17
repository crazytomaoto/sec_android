package com.hualianzb.sec.api;


import com.hysd.android.platform_huanuo.net.base.ProtocolType;

/**
 * FileName    : IReturnCallback.java
 * Description : 业务层的数据返回
 *
 * @author : 王天运
 * @version : 1.0
 * Create Date : 2014-4-14 下午3:03:59
 * @Copyright : GL. All Rights Reserved
 * @Company :
 **/
public interface IReturnCallback<T extends CommonResult> {

    public void onReturn(Object invoker, ProtocolType.ResponseEvent event, T result);
}
