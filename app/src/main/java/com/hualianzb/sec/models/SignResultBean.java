package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2019/1/15
 * auther:wangtianyun
 * describe:
 */
public class SignResultBean implements Serializable {
    /**
     * timestamp : 1547543042033
     * from : 84711b41aee43001a2474e3633460024390de3af
     * to : e2292bd85e9a4bc2994ec4e97e59d9a854e59dc9
     * value : 0.1
     * contractAddress :
     * gasLimit : 0
     * gas : 0
     * gasPrice : 0
     * inputData : 哈哈
     * data : {"v":27,"r":"f3a52fbc9b95236ec0b5c746584a8729591124c0fb0b846260b704fa7add0a66","s":"048618369451c5cc12be563bafc33949d6c260f4d4267f60aa4447044010b8e2"}
     */

    private long timestamp;
    private String from;
    private String to;
    private String value;
    private String contractAddress;
    private String gasLimit;
    private String gas;
    private String gasPrice;
    private String inputData;
    private DataBean data;

    public SignResultBean() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
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

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * v : 27
         * r : f3a52fbc9b95236ec0b5c746584a8729591124c0fb0b846260b704fa7add0a66
         * s : 048618369451c5cc12be563bafc33949d6c260f4d4267f60aa4447044010b8e2
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
