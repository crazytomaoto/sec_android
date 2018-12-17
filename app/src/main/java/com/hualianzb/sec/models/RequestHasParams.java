package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/8/30
 * auther:wangtianyun
 * describe:
 */
public class RequestHasParams implements Serializable {
    /**
     * jsonrpc : 2.0
     * method : eth_getTransactionByHash
     * params : ["0x88df016429689c079f3b2f6ad39fa052532c56795b733da78a91ebe6a713944b"]
     * id : 1
     */

    private String jsonrpc;
    private String method;
    private int id;
    private List<String> params;

    public RequestHasParams() {
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
