package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/10/10
 * auther:wangtianyun
 * describe:
 */
public class AllTransBean implements Serializable {
    public AllTransBean() {
    }

    private List<ResultBean> resultBeanLast;

    public List<ResultBean> getResultBeanLast() {
        return resultBeanLast;
    }

    public void setResultBeanLast(List<ResultBean> resultBeanLast) {
        this.resultBeanLast = resultBeanLast;
    }

    public static class ResultBean implements Serializable {
        public ResultBean() {
        }


        private String blockNumber;
        private String blockHash;
        private String timeStamp;
        private String hash;
        private String nonce;
        private String transactionIndex;
        private String from;
        private String to;
        private String value;
        private String gas;//limit
        private String gasPrice;
        private String input;
        private String gasUsed;
        private String kind;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
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

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }
    }
}
