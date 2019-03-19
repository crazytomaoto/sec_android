package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2019/1/22
 * auther:wangtianyun
 * describe:
 */
public class ManageWalletBean implements Serializable {
    public ManageWalletBean() {
    }

    private String walletName;
    private String address;
    private int howToCreate;
    private boolean isBackup;
    private String money;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
