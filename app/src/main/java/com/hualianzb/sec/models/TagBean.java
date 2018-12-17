package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/10/10
 * auther:wangtianyun
 * describe:
 */
public class TagBean implements Serializable {
    public TagBean() {
    }

    private TransactionByHashBean.ResultBean bean;
    private String kind;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public TransactionByHashBean.ResultBean getBean() {
        return bean;
    }

    public void setBean(TransactionByHashBean.ResultBean bean) {
        this.bean = bean;
    }
}
