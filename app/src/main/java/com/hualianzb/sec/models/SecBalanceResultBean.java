package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/11/23
 * auther:wangtianyun
 * describe:
 */
public class SecBalanceResultBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * id : 1
     * result : {"status":"1","info":"OK","value":5.076930441596268}
     */

    private String jsonrpc;
    private int id;
    private ResultBean result;

    public SecBalanceResultBean() {
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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        public ResultBean() {
        }

        /**
         * status : 1
         * info : OK
         * value : 5.076930441596268
         */

        private String status;
        private String info;
        private String value;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
