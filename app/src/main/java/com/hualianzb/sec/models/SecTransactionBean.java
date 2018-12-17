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
     * result : {"status":"1","message":"OK","resultInChain":[{"Version":"0.1","TimeStamp":1543458300736,"TxFrom":"53a801c4da2cc72cf6be348369678b6f86c5edc1","TxTo":"fa9461cc20fbb1b0937aa07ec6afc5e660fe2afd","Value":"100000000","GasLimit":"0","GasUsedByTxn":"0","GasPrice":"0","Nonce":"955","InputData":"Mobile APP JSONRPC API Function Test","Signature":{"r":{"type":"Buffer","data":[161,156,90,130,29,179,161,185,80,54,75,144,62,106,230,131,245,94,20,42,140,35,231,174,79,124,30,84,112,54,163,60]},"s":{"type":"Buffer","data":[115,198,108,132,38,225,177,93,91,39,162,226,80,5,235,109,113,103,221,214,248,186,97,192,77,18,157,122,215,245,85,182]},"v":28},"TxHash":"88f7c87cdd87fa17a5523b8148426266a4db19bd155e735eb814870bd12262f5","TxFee":"0","TxReceiptStatus":"success","BlockNumber":1000,"BlockHash":"f0c1270b8b4b3935fe2cdb5a0878f349b3bcee3ddb1a724d3722a361582495bf","CumulativeGasUsed":4845,"TransactionIndex":8,"ContractAddress":"","Confirmations":""}],"resultInPool":[]}
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
        public ResultBean() {
        }

        /**
         * status : 1
         * message : OK
         * resultInChain : [{"Version":"0.1","TimeStamp":1543458300736,"TxFrom":"53a801c4da2cc72cf6be348369678b6f86c5edc1","TxTo":"fa9461cc20fbb1b0937aa07ec6afc5e660fe2afd","Value":"100000000","GasLimit":"0","GasUsedByTxn":"0","GasPrice":"0","Nonce":"955","InputData":"Mobile APP JSONRPC API Function Test","Signature":{"r":{"type":"Buffer","data":[161,156,90,130,29,179,161,185,80,54,75,144,62,106,230,131,245,94,20,42,140,35,231,174,79,124,30,84,112,54,163,60]},"s":{"type":"Buffer","data":[115,198,108,132,38,225,177,93,91,39,162,226,80,5,235,109,113,103,221,214,248,186,97,192,77,18,157,122,215,245,85,182]},"v":28},"TxHash":"88f7c87cdd87fa17a5523b8148426266a4db19bd155e735eb814870bd12262f5","TxFee":"0","TxReceiptStatus":"success","BlockNumber":1000,"BlockHash":"f0c1270b8b4b3935fe2cdb5a0878f349b3bcee3ddb1a724d3722a361582495bf","CumulativeGasUsed":4845,"TransactionIndex":8,"ContractAddress":"","Confirmations":""}]
         * resultInPool : []
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

        public void setResultInChain(List<ResultInChainBeanOrPool> resultInChainBeanOrPool) {
            this.resultInChain = resultInChainBeanOrPool;
        }

        public List<?> getResultInPool() {
            return resultInPool;
        }

        public void setResultInPool(List<ResultInChainBeanOrPool> resultInPool) {
            this.resultInPool = resultInPool;
        }

        public static class ResultInChainBeanOrPool implements Serializable {
            public ResultInChainBeanOrPool() {
            }

            /**
             * Version : 0.1
             * TimeStamp : 1543458300736
             * TxFrom : 53a801c4da2cc72cf6be348369678b6f86c5edc1
             * TxTo : fa9461cc20fbb1b0937aa07ec6afc5e660fe2afd
             * Value : 100000000
             * GasLimit : 0
             * GasUsedByTxn : 0
             * GasPrice : 0
             * Nonce : 955
             * InputData : Mobile APP JSONRPC API Function Test
             * Signature : {"r":{"type":"Buffer","data":[161,156,90,130,29,179,161,185,80,54,75,144,62,106,230,131,245,94,20,42,140,35,231,174,79,124,30,84,112,54,163,60]},"s":{"type":"Buffer","data":[115,198,108,132,38,225,177,93,91,39,162,226,80,5,235,109,113,103,221,214,248,186,97,192,77,18,157,122,215,245,85,182]},"v":28}
             * TxHash : 88f7c87cdd87fa17a5523b8148426266a4db19bd155e735eb814870bd12262f5
             * TxFee : 0
             * TxReceiptStatus : success
             * BlockNumber : 1000
             * BlockHash : f0c1270b8b4b3935fe2cdb5a0878f349b3bcee3ddb1a724d3722a361582495bf
             * CumulativeGasUsed : 4845
             * TransactionIndex : 8
             * ContractAddress :
             * Confirmations :
             */

            private String Version;
            private long TimeStamp;
            private String TxFrom;
            private String TxTo;
            private String Value;
            private String GasLimit;
            private String GasUsedByTxn;
            private String GasPrice;
            private String Nonce;
            private String InputData;
            private SignatureBean Signature;
            private String TxHash;
            private String TxFee;
            private String TxReceiptStatus;
            private int BlockNumber;
            private String BlockHash;
            private int CumulativeGasUsed;
            private int TransactionIndex;
            private String ContractAddress;
            private String Confirmations;

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

            public String getTxHash() {
                return TxHash;
            }

            public void setTxHash(String TxHash) {
                this.TxHash = TxHash;
            }

            public String getTxFee() {
                return TxFee;
            }

            public void setTxFee(String TxFee) {
                this.TxFee = TxFee;
            }

            public String getTxReceiptStatus() {
                return TxReceiptStatus;
            }

            public void setTxReceiptStatus(String TxReceiptStatus) {
                this.TxReceiptStatus = TxReceiptStatus;
            }

            public int getBlockNumber() {
                return BlockNumber;
            }

            public void setBlockNumber(int BlockNumber) {
                this.BlockNumber = BlockNumber;
            }

            public String getBlockHash() {
                return BlockHash;
            }

            public void setBlockHash(String BlockHash) {
                this.BlockHash = BlockHash;
            }

            public int getCumulativeGasUsed() {
                return CumulativeGasUsed;
            }

            public void setCumulativeGasUsed(int CumulativeGasUsed) {
                this.CumulativeGasUsed = CumulativeGasUsed;
            }

            public int getTransactionIndex() {
                return TransactionIndex;
            }

            public void setTransactionIndex(int TransactionIndex) {
                this.TransactionIndex = TransactionIndex;
            }

            public String getContractAddress() {
                return ContractAddress;
            }

            public void setContractAddress(String ContractAddress) {
                this.ContractAddress = ContractAddress;
            }

            public String getConfirmations() {
                return Confirmations;
            }

            public void setConfirmations(String Confirmations) {
                this.Confirmations = Confirmations;
            }

            public static class SignatureBean {
                /**
                 * r : {"type":"Buffer","data":[161,156,90,130,29,179,161,185,80,54,75,144,62,106,230,131,245,94,20,42,140,35,231,174,79,124,30,84,112,54,163,60]}
                 * s : {"type":"Buffer","data":[115,198,108,132,38,225,177,93,91,39,162,226,80,5,235,109,113,103,221,214,248,186,97,192,77,18,157,122,215,245,85,182]}
                 * v : 28
                 */

                private RBean r;
                private SBean s;
                private int v;

                public RBean getR() {
                    return r;
                }

                public void setR(RBean r) {
                    this.r = r;
                }

                public SBean getS() {
                    return s;
                }

                public void setS(SBean s) {
                    this.s = s;
                }

                public int getV() {
                    return v;
                }

                public void setV(int v) {
                    this.v = v;
                }

                public static class RBean {
                    /**
                     * type : Buffer
                     * data : [161,156,90,130,29,179,161,185,80,54,75,144,62,106,230,131,245,94,20,42,140,35,231,174,79,124,30,84,112,54,163,60]
                     */

                    private String type;
                    private List<Integer> data;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public List<Integer> getData() {
                        return data;
                    }

                    public void setData(List<Integer> data) {
                        this.data = data;
                    }
                }

                public static class SBean {
                    /**
                     * type : Buffer
                     * data : [115,198,108,132,38,225,177,93,91,39,162,226,80,5,235,109,113,103,221,214,248,186,97,192,77,18,157,122,215,245,85,182]
                     */

                    private String type;
                    private List<Integer> data;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public List<Integer> getData() {
                        return data;
                    }

                    public void setData(List<Integer> data) {
                        this.data = data;
                    }
                }
            }
        }
    }
}
