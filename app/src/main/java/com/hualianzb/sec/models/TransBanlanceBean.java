package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/9/29
 * auther:wangtianyun
 * describe:
 */
public class TransBanlanceBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * method : eth_sendTransaction
     * params : [{"from":"0x627306090abaB3A6e1400e9345bC60c78a8BEf57","to":"0xf17f52151EbEF6C7334FAD080c5704D77216b732","gas":"0x76c0","gasPrice":"0x9184e72a000","value":"0x8AC7230489E80000","data":"0x0"}]
     * id : 3
     */

    private String jsonrpc;
    private String method;
    private int id;
    private List<ParamsBean> params;

    public TransBanlanceBean() {
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
        /**
         * from : 0x627306090abaB3A6e1400e9345bC60c78a8BEf57        // 转出帐户
         * to : 0xf17f52151EbEF6C7334FAD080c5704D77216b732          // 转入帐户
         * gas : 0x76c0                                                 // gas
         * gasPrice : 0x9184e72a000                                     // gas单价
         * value : 0x8AC7230489E80000                             // 转账金额
         * data : 0x0
         */

        private String from;
        private String to;
        private String gas;
        private String gasPrice;
        private String value;
        private String data;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
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

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
