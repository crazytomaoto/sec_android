package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/10/9
 * auther:wangtianyun
 * describe:
 */
public class EthTransLogBean implements Serializable {
    /**
     * status : 1
     * message : OK
     * result : [{"blockNumber":"3071019","timeStamp":"1538192273","hash":"0x35f969f74491d82c60cc3c72234463b5b5f78cdc4ba1df0c170ac782116e3095","nonce":"64","blockHash":"0x20cf813f09c48abb62508808ab4d2f8aadd6835ca323d64ef8af364826f5aa25","transactionIndex":"6","from":"0xe2292bd85e9a4bc2994ec4e97e59d9a854e59dc9","to":"0x5a3f404e2b687f94fcff550a69eb077cdd963236","value":"0","gas":"1058400","gasPrice":"1000000000","isError":"0","txreceipt_status":"1","input":"0xa9059cbb00000000000000000000000000d35564dd1105768fd05d52683a51e14cf6d6b5000000000000000000000000000000000000000000000000016345785d8a0000","contractAddress":"","cumulativeGasUsed":"302315","gasUsed":"37281","confirmations":"57383"}]
     */

    private String status;
    private String message;
    private List<ResultBean> result;

    public EthTransLogBean() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * blockNumber : 3071019
         * timeStamp : 1538192273
         * hash : 0x35f969f74491d82c60cc3c72234463b5b5f78cdc4ba1df0c170ac782116e3095
         * nonce : 64
         * blockHash : 0x20cf813f09c48abb62508808ab4d2f8aadd6835ca323d64ef8af364826f5aa25
         * transactionIndex : 6
         * from : 0xe2292bd85e9a4bc2994ec4e97e59d9a854e59dc9
         * to : 0x5a3f404e2b687f94fcff550a69eb077cdd963236
         * value : 0
         * gas : 1058400
         * gasPrice : 1000000000
         * isError : 0
         * txreceipt_status : 1
         * input : 0xa9059cbb00000000000000000000000000d35564dd1105768fd05d52683a51e14cf6d6b5000000000000000000000000000000000000000000000000016345785d8a0000
         * contractAddress :
         * cumulativeGasUsed : 302315
         * gasUsed : 37281
         * confirmations : 57383
         */

        private String blockNumber;
        private String timeStamp;
        private String hash;
        private String nonce;
        private String blockHash;
        private String transactionIndex;
        private String from;
        private String to;
        private String value;
        private String gas;
        private String gasPrice;
        private String isError;
        private String txreceipt_status;
        private String input;
        private String contractAddress;
        private String cumulativeGasUsed;
        private String gasUsed;
        private String confirmations;

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
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

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
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

        public String getIsError() {
            return isError;
        }

        public void setIsError(String isError) {
            this.isError = isError;
        }

        public String getTxreceipt_status() {
            return txreceipt_status;
        }

        public void setTxreceipt_status(String txreceipt_status) {
            this.txreceipt_status = txreceipt_status;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getCumulativeGasUsed() {
            return cumulativeGasUsed;
        }

        public void setCumulativeGasUsed(String cumulativeGasUsed) {
            this.cumulativeGasUsed = cumulativeGasUsed;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(String confirmations) {
            this.confirmations = confirmations;
        }
    }
}
