package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/8/30
 * auther:wangtianyun
 * describe:
 */
public class TransRecorderBeanOne implements Serializable {
    /**
     * jsonrpc : 2.0
     * id : 74
     * result : [{"address":"0x86f8aac29330d978bd398f9b0cb83fca13e1d9d9","blockHash":"0x45730761b66695bd7137d3da16353f7dd4b7c1578b284a2420f89640c75d7f86","blockNumber":"0x2c383e","data":"0x0000000000000000000000000000000000000000000000000000000000000002","logIndex":"0x9","removed":false,"topics":["0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef","0x0000000000000000000000000000000000000000000000000000000000000000","0x00000000000000000000000049dff16628c268ed6fbf9b728e6f8a754a08e065"],"transactionHash":"0x0faa563d415fb229121e0b168e6d56f5d96d1c1039ee20353c75ada39c8ffeb3","transactionIndex":"0x7"}]
     */

    private String jsonrpc;
    private int id;
    private List<ResultBean> result;

    public TransRecorderBeanOne() {
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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public class ResultBean implements Serializable {
        public ResultBean() {
        }

        /**
         * address : 0x86f8aac29330d978bd398f9b0cb83fca13e1d9d9
         * blockHash : 0x45730761b66695bd7137d3da16353f7dd4b7c1578b284a2420f89640c75d7f86
         * blockNumber : 0x2c383e
         * data : 0x0000000000000000000000000000000000000000000000000000000000000002
         * logIndex : 0x9
         * removed : false
         * topics : ["0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef","0x0000000000000000000000000000000000000000000000000000000000000000","0x00000000000000000000000049dff16628c268ed6fbf9b728e6f8a754a08e065"]
         * transactionHash : 0x0faa563d415fb229121e0b168e6d56f5d96d1c1039ee20353c75ada39c8ffeb3
         * transactionIndex : 0x7
         */
        private String kind;//-----------哪种钱包
        private String address;
        private String blockHash;
        private String blockNumber;
        private String data;
        private String logIndex;
        private boolean removed;
        private String transactionHash;
        private String transactionIndex;
        private List<String> topics;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

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

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getLogIndex() {
            return logIndex;
        }

        public void setLogIndex(String logIndex) {
            this.logIndex = logIndex;
        }

        public boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public List<String> getTopics() {
            return topics;
        }

        public void setTopics(List<String> topics) {
            this.topics = topics;
        }
    }
}
