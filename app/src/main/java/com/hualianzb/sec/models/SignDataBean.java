package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2019/1/15
 * auther:wangtianyun
 * describe:
 */
public class SignDataBean implements Serializable {
    /**
     * privateKey : bad50f54db86259e077749d2593cd0fc74550a9b303da02972a37a16a7d23819
     * from : 0858768edb7c24b329efd1133888c2e1d0c23e76
     * to : 04d7e0cd097bf5da8a6ac64b333d606639ffd7e8
     * value : 3
     * inputData : Test
     */

    private String privateKey;
    private String from;
    private String to;
    private String value;
    private String inputData;

    public SignDataBean() {
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
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

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }
}
