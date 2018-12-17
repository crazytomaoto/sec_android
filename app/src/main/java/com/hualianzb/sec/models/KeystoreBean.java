package com.hualianzb.sec.models;

import org.web3j.crypto.ECKeyPair;

import java.io.Serializable;

/**
 * Date:2018/8/17
 * auther:wangtianyun
 * describe:
 */
public class KeystoreBean implements Serializable {
    public KeystoreBean() {
    }

    private String keystore;
    private ECKeyPair keyPair;

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public ECKeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(ECKeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
