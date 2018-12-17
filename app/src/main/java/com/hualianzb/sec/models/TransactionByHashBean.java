package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/8/30
 * auther:wangtianyun
 * describe:
 */
public class TransactionByHashBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * id : 1
     * result : {"blockHash":"0x1d59ff54b1eb26b013ce3cb5fc9dab3705b415a67127a003c3e61eb445bb8df2","blockNumber":"0x5daf3b","from":"0xa7d9ddbe1f17865597fbd27ec712455208b6b76d","gas":"0xc350","gasPrice":"0x4a817c800","hash":"0x88df016429689c079f3b2f6ad39fa052532c56795b733da78a91ebe6a713944b","input":"0x68656c6c6f21","nonce":"0x15","to":"0xf02c1c8e6114b1dbe8937a39260b5b0a374432bb","transactionIndex":"0x41","value":"0xf3dbb76162000","v":"0x25","r":"0x1b5e176d927f8e9ab405058b2d2457392da3e20f328b16ddabcebc33eaac5fea","s":"0x4ba69724e8f69de52f0125ad8b3c5c2cef33019bac3249e2c0a2192766d1721c"}
     */

    private String jsonrpc;
    private int id;
    private ResultBean result;

    public TransactionByHashBean() {
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
        /**
         * blockHash : 0x1d59ff54b1eb26b013ce3cb5fc9dab3705b415a67127a003c3e61eb445bb8df2
         * blockNumber : 0x5daf3b
         * from : 0xa7d9ddbe1f17865597fbd27ec712455208b6b76d
         * gas : 0xc350
         * gasPrice : 0x4a817c800
         * hash : 0x88df016429689c079f3b2f6ad39fa052532c56795b733da78a91ebe6a713944b
         * input : 0x68656c6c6f21
         * nonce : 0x15
         * to : 0xf02c1c8e6114b1dbe8937a39260b5b0a374432bb
         * transactionIndex : 0x41
         * value : 0xf3dbb76162000
         * v : 0x25
         * r : 0x1b5e176d927f8e9ab405058b2d2457392da3e20f328b16ddabcebc33eaac5fea
         * s : 0x4ba69724e8f69de52f0125ad8b3c5c2cef33019bac3249e2c0a2192766d1721c
         */

        private String blockHash;
        private String blockNumber;
        private String from;
        private String gas;
        private String gasPrice;
        private String hash;//交易的哈希
        private String input;
        private String nonce;
        private String to;
        private String transactionIndex;
        private String value;
        private String v;
        private String r;
        private String s;

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
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

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
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
