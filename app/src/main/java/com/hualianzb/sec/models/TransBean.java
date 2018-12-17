package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/8/28
 * auther:wangtianyun
 * describe:
 */
public class TransBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * method : eth_sendRawTransaction
     * params : ["0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675"]
     * id : 1
     */

    private String jsonrpc;
    private String method;
    private int id;
    private List<String> params;

    public TransBean() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
