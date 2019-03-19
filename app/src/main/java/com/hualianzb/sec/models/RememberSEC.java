package com.hualianzb.sec.models;

import java.io.Serializable;

public class RememberSEC implements Serializable {
    public RememberSEC() {
    }

    private String walletName;
    private String address;
    private String pass;
    private String mnemonics;
    private String walletFile;
    private String tips;
    private String privateKey;
    private String publicKey;
    private int walletincon;//钱包头像
    private boolean isNow;//是否当前钱包
    private boolean isBackup;//是否已经备份
    private int howToCreate;//1，创建的  2助记词导入的 3私钥导入的，4 keystore导入的
    private String creatTime;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public int getHowToCreate() {
        return howToCreate;
    }

    public void setHowToCreate(int howToCreate) {
        this.howToCreate = howToCreate;
    }

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }

    public int getWalletincon() {
        return walletincon;
    }

    public void setWalletincon(int walletincon) {
        this.walletincon = walletincon;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMnemonics() {
        return mnemonics;
    }

    public void setMnemonics(String mnemonics) {
        this.mnemonics = mnemonics;
    }

    public String getWalletFile() {
        return walletFile;
    }

    public void setWalletFile(String walletFile) {
        this.walletFile = walletFile;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isNow() {
        return isNow;
    }

    public void setNow(boolean now) {
        isNow = now;
    }
}
