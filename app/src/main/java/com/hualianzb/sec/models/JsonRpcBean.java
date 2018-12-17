package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/8/25
 * auther:wangtianyun
 * describe:
 */
public class JsonRpcBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * method : eth_getLogs
     * params : [{"topics":["0x241ea03ca20251805084d27d4440371c34a0b85ff108f6bb5611248f73818b80"]}]
     * id : 1
     */

    private String jsonrpc;
    private String method;
    private int id;
    private List<ParamsBean> params;

    public JsonRpcBean() {
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

    public List<ParamsBean> getParams() {
        return params;
    }

    public void setParams(List<ParamsBean> params) {
        this.params = params;
    }

    public static class ParamsBean implements Serializable {
        private String address;

        private String fromBlock;
        private List<String> topics;

        public List<String> getTopics() {
            return topics;
        }

        public void setTopics(List<String> topics) {
            this.topics = topics;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFromBlock() {
            return fromBlock;
        }

        public void setFromBlock(String fromBlock) {
            this.fromBlock = fromBlock;
        }
    }
}
