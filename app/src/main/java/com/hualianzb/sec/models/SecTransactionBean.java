package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/11/28
 * auther:wangtianyun
 * describe:
 */
public class SecTransactionBean implements Serializable {
    /**
     * jsonrpc : 2.0
     * id : 1
     * result : {"status":"1","message":"OK","resultInChain":[],"resultInPool":[{"TxHash":"8e7dee2e8bdd0fd2d741a4f727b50477f75af334e2e35e213b6ca9935c0d7e8c","TxReceiptStatus":"pending","Version":"0.1","TimeStamp":1547603210293,"TxFrom":"31974771bdb940515fb09d72971b5aecfd86c904","TxTo":"84711b41aee43001a2474e3633460024390de3af","Value":"1.1","ContractAddress":"","GasLimit":"0","GasUsedByTxn":"0","GasPrice":"0","TxFee":"0","Nonce":"0","InputData":"布达佩斯","Signature":{"r":"e5af1708bb9c3ee4aebfb1bac53e05675f02a798a700cfc2a5d0824389c6cb0d","s":"40b74ec5b6558dcb69792cd461dbe31358766233a7ee686eaf03d0cda3dc4bf7","v":27},"TxHeight":0}]}
     */

    private String jsonrpc;
    private int id;
    private ResultBean result;

    public SecTransactionBean() {
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
         * status : 1
         * message : OK
         * resultInChain : []
         * resultInPool : [{"TxHash":"8e7dee2e8bdd0fd2d741a4f727b50477f75af334e2e35e213b6ca9935c0d7e8c","TxReceiptStatus":"pending","Version":"0.1","TimeStamp":1547603210293,"TxFrom":"31974771bdb940515fb09d72971b5aecfd86c904","TxTo":"84711b41aee43001a2474e3633460024390de3af","Value":"1.1","ContractAddress":"","GasLimit":"0","GasUsedByTxn":"0","GasPrice":"0","TxFee":"0","Nonce":"0","InputData":"布达佩斯","Signature":{"r":"e5af1708bb9c3ee4aebfb1bac53e05675f02a798a700cfc2a5d0824389c6cb0d","s":"40b74ec5b6558dcb69792cd461dbe31358766233a7ee686eaf03d0cda3dc4bf7","v":27},"TxHeight":0}]
         */

        private String status;
        private String message;
        private List<ResultInChainBeanOrPool> resultInChain;
        private List<ResultInChainBeanOrPool> resultInPool;

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

        public List<ResultInChainBeanOrPool> getResultInChain() {
            return resultInChain;
        }

        public void setResultInChain(List<ResultInChainBeanOrPool> resultInChain) {
            this.resultInChain = resultInChain;
        }

        public List<ResultInChainBeanOrPool> getResultInPool() {
            return resultInPool;
        }

        public void setResultInPool(List<ResultInChainBeanOrPool> resultInPool) {
            this.resultInPool = resultInPool;
        }

        public static class ResultInChainBeanOrPool implements Serializable {
            /**
             * TxHash : 8e7dee2e8bdd0fd2d741a4f727b50477f75af334e2e35e213b6ca9935c0d7e8c
             * TxReceiptStatus : pending
             * Version : 0.1
             * TimeStamp : 1547603210293
             * TxFrom : 31974771bdb940515fb09d72971b5aecfd86c904
             * TxTo : 84711b41aee43001a2474e3633460024390de3af
             * Value : 1.1
             * ContractAddress :
             * GasLimit : 0
             * GasUsedByTxn : 0
             * GasPrice : 0
             * TxFee : 0
             * Nonce : 0
             * InputData : 布达佩斯
             * Signature : {"r":"e5af1708bb9c3ee4aebfb1bac53e05675f02a798a700cfc2a5d0824389c6cb0d","s":"40b74ec5b6558dcb69792cd461dbe31358766233a7ee686eaf03d0cda3dc4bf7","v":27}
             * TxHeight : 0
             */

            private String TxHash;
            private String TxReceiptStatus;
            private String Version;
            private long TimeStamp;
            private String TxFrom;
            private String TxTo;
            private String Value;
            private String ContractAddress;
            private String GasLimit;
            private String GasUsedByTxn;
            private String GasPrice;
            private String TxFee;
            private String Nonce;
            private String InputData;
            private SignatureBean Signature;
            private int TxHeight;

            public String getTxHash() {
                return TxHash;
            }

            public void setTxHash(String TxHash) {
                this.TxHash = TxHash;
            }

            public String getTxReceiptStatus() {
                return TxReceiptStatus;
            }

            public void setTxReceiptStatus(String TxReceiptStatus) {
                this.TxReceiptStatus = TxReceiptStatus;
            }

            public String getVersion() {
                return Version;
            }

            public void setVersion(String Version) {
                this.Version = Version;
            }

            public long getTimeStamp() {
                return TimeStamp;
            }

            public void setTimeStamp(long TimeStamp) {
                this.TimeStamp = TimeStamp;
            }

            public String getTxFrom() {
                return TxFrom;
            }

            public void setTxFrom(String TxFrom) {
                this.TxFrom = TxFrom;
            }

            public String getTxTo() {
                return TxTo;
            }

            public void setTxTo(String TxTo) {
                this.TxTo = TxTo;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                this.Value = Value;
            }

            public String getContractAddress() {
                return ContractAddress;
            }

            public void setContractAddress(String ContractAddress) {
                this.ContractAddress = ContractAddress;
            }

            public String getGasLimit() {
                return GasLimit;
            }

            public void setGasLimit(String GasLimit) {
                this.GasLimit = GasLimit;
            }

            public String getGasUsedByTxn() {
                return GasUsedByTxn;
            }

            public void setGasUsedByTxn(String GasUsedByTxn) {
                this.GasUsedByTxn = GasUsedByTxn;
            }

            public String getGasPrice() {
                return GasPrice;
            }

            public void setGasPrice(String GasPrice) {
                this.GasPrice = GasPrice;
            }

            public String getTxFee() {
                return TxFee;
            }

            public void setTxFee(String TxFee) {
                this.TxFee = TxFee;
            }

            public String getNonce() {
                return Nonce;
            }

            public void setNonce(String Nonce) {
                this.Nonce = Nonce;
            }

            public String getInputData() {
                return InputData;
            }

            public void setInputData(String InputData) {
                this.InputData = InputData;
            }

            public SignatureBean getSignature() {
                return Signature;
            }

            public void setSignature(SignatureBean Signature) {
                this.Signature = Signature;
            }

            public int getTxHeight() {
                return TxHeight;
            }

            public void setTxHeight(int TxHeight) {
                this.TxHeight = TxHeight;
            }

            public static class SignatureBean implements Serializable {
                /**
                 * r : e5af1708bb9c3ee4aebfb1bac53e05675f02a798a700cfc2a5d0824389c6cb0d
                 * s : 40b74ec5b6558dcb69792cd461dbe31358766233a7ee686eaf03d0cda3dc4bf7
                 * v : 27
                 */

                private String r;
                private String s;
                private int v;

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

                public int getV() {
                    return v;
                }

                public void setV(int v) {
                    this.v = v;
                }
            }
        }
    }
}
