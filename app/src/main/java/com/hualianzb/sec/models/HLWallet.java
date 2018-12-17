package com.hualianzb.sec.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hualianzb.sec.commons.constants.Constant;
import org.web3j.crypto.WalletFile;

public class HLWallet {

    public WalletFile walletFile;

    @JsonIgnore
    public boolean isCurrent = false;

    public HLWallet() {

    }

    public HLWallet(WalletFile walletFile) {
        this.walletFile = walletFile;
    }

    public String getAddress(){
        return Constant.PREFIX_16 + this.walletFile.getAddress();
    }



}
