package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/9/14
 * auther:wangtianyun
 * describe:
 */
public class TransResultBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * id : 1
     * result : 0x288b66c54055f279f1a8ca837b0f303112a61560d95d7f6bf2eec0dd83ee0ddd
     */

    private String jsonrpc;
    private int id;
    private String result;

    public TransResultBean() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
