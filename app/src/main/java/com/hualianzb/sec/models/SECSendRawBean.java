package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/12/1
 * auther:wangtianyun
 * describe:
 */
public class SECSendRawBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * method : sec_sendRawTransaction
     * params : [{"from":"0xb60e8dd61c5d32be8058bb8eb970870f07233155","inputData":"Sec test transaction","timestamp":1543457005562,"gasLimit":"0","to":"0xd46e8dd67c5d32be8058bb8eb970870f07244567","gas":"0x76c0","gasPrice":"0x9184e72a000","value":"0x9184e72a","data":{"v":28,"r":"f17c29dd068953a474675a65f59c75c6189c426d1c60f43570cc7220ca3616c3","s":"54f9ff243b903b7419dd566f277eedadf6aa55161f5d5e42005af29b14577902"}}]
     * id : 1
     */

    private String jsonrpc;
    private String method;
    private int id;
    private List<ParamsBean> params;

    public SECSendRawBean() {
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
        public ParamsBean() {
        }

        /**
         * from : 0xb60e8dd61c5d32be8058bb8eb970870f07233155
         * timestamp : 1543457005562
         * gasLimit : 0
         * to : 0xd46e8dd67c5d32be8058bb8eb970870f07244567
         * gas : 0x76c0
         * gasPrice : 0x9184e72a000
         * value : 0x9184e72a
         * inputData:"sec test input"
         * data : {"v":28,"r":"f17c29dd068953a474675a65f59c75c6189c426d1c60f43570cc7220ca3616c3","s":"54f9ff243b903b7419dd566f277eedadf6aa55161f5d5e42005af29b14577902"}
         */

        private String from;
        private long timestamp;
        private String gasLimit;
        private String to;
        private String gas;
        private String gasPrice;
        private String value;
        private String inputData;
        private DataBean data;

        public String getInputData() {
            return inputData;
        }

        public void setInputData(String inputData) {
            this.inputData = inputData;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getGasLimit() {
            return gasLimit;
        }

        public void setGasLimit(String gasLimit) {
            this.gasLimit = gasLimit;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            public DataBean() {
            }

            /**
             * v : 28
             * r : f17c29dd068953a474675a65f59c75c6189c426d1c60f43570cc7220ca3616c3
             * s : 54f9ff243b903b7419dd566f277eedadf6aa55161f5d5e42005af29b14577902
             */

            private int v;
            private String r;
            private String s;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }

            public String getR() {
                return r;
            }

            public void setR(String r) {
                this.r = r;
            }

            public String getS() {
                return s;
            }

            public void setS(String s) {
                this.s = s;
            }
        }
    }
}
