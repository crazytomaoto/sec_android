package com.hualianzb.sec.api.request;

import com.hualianzb.sec.api.BaseRequest;
import com.hualianzb.sec.api.IReturnCallback;
import com.hysd.android.platform_huanuo.net.base.ResultItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Filename		: DynaRequest.java
 *
 * @Description :
 * @Author : 王天运
 * @Version ：1.0
 * @Date ：2015年11月17日 上午11:54:38
 */
public class DynaRequest extends BaseRequest<DynaCommonResult> {

    /**
     * 请求的参数
     */
    protected List<Object> paramValues = new ArrayList<Object>();
    /**
     * 参数添加的顺序
     */
    protected List<String> paramNames = new ArrayList<String>();

    private String model;

    public DynaRequest(Object invoker, IReturnCallback<DynaCommonResult> callBackx) {
        super(invoker, callBackx);
    }

    @Override
    protected DynaCommonResult getResultObj() {
        return new DynaCommonResult();
    }

    @Override
    protected void buildParams() {
        int i = 0;
        for (String key : paramNames) {
            request.addParam(key, paramValues.get(i));
            i++;
        }
    }

    @Override
    protected void parseData(DynaCommonResult result, ResultItem item) {
        result.data = item;
    }

    @Override
    protected String getTypeURL() {
        return model;
    }

    /**
     * 添加基本的参数
     */
    public void addParam(String key, Object value) {
        this.paramNames.add(key);
        this.paramValues.add(value);
    }

    public void setModel(String model) {
        this.model = model;
    }

}
