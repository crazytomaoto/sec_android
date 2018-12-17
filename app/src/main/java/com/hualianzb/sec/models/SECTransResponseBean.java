package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/12/1
 * auther:wangtianyun
 * describe:
 */
public class SECTransResponseBean implements Serializable {
    /**
     * id : 1
     * jsonrpc : 2.0
     * result : {"status":"1","info":"OK","txHash":"0xa9be18026c4c6d835d95ca94583f18100ff56a696855902e195a6f4881448a39"}
     */

    private int id;
    private String jsonrpc;
    private ResultBean result;

    public SECTransResponseBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        public ResultBean() {
        }

        /**
         * status : 1
         * info : OK
         * txHash : 0xa9be18026c4c6d835d95ca94583f18100ff56a696855902e195a6f4881448a39
         */

        private String status;
        private String info;
        private String txHash;

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

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }
    }
}
