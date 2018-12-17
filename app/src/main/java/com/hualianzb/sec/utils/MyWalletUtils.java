package com.hualianzb.sec.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.IOException;

/**
 * Date:2018/11/7
 * auther:wangtianyun
 * describe:
 */
public class MyWalletUtils {
    public static Credentials loadCredentials(String password, File source) throws IOException, CipherException {
        WalletFile walletFile = new ObjectMapper().readValue(source, WalletFile.class);
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }
}
