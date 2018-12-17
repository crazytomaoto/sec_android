package com.hualianzb.sec.utils;

import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.RememberEth;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * Date:2018/8/21
 * auther:wangtianyun
 * describe:
 */
public class MyWeb3jUtil {
    public static Web3j getWeb3jInstance() {
        Web3j web3j = Web3jFactory.build(new HttpService(RequestHost.url));
        return web3j;
    }

    public static String signedEthTransactionData(RememberEth rememberEth, String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, double amount) throws Exception { // 把十进制的转换成ETH的Wei, 1ETH = 10^18 Wei
        BigDecimal realValue = BigDecimal.valueOf(amount * Math.pow(10.0, 9));

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, realValue.toBigInteger());
        return signData(rawTransaction, rememberEth);
    }

    private static String signData(RawTransaction rawTransaction, RememberEth rememberEth) throws Exception {
//        WalletFile walletFile = JSON.parseObject(rememberEth.getWalletFile(), WalletFile.class);
        String lastString = null;
        ECKeyPair ecKeyPair = null;
        new Thread() {
            @Override
            public void run() {
                super.run();
                ECKeyPair ecKeyPair = OwnWalletUtils.getKeyPair(rememberEth.getPrivateKey());
                if (null != ecKeyPair) {
                    rememberEth.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                    rememberEth.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                    Credentials credentials = Credentials.create(ecKeyPair);
                    byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, ChainId.MAINNET, credentials);
                }
            }
        }.start();

//
        //生成凭证
        Credentials credentials = Credentials.create(ecKeyPair);
//        Credentials credentials = Credentials.create(Wallet.decrypt(rememberEth.getPass(), walletFile));

//                Credentials.create(rememberEth.getPass(), walletFile);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, ChainId.MAINNET, credentials);
        return Numeric.toHexString(signMessage);
    }

    public static String signContractTransaction(RememberEth rememberEth, String contractAddress, String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, Double amount) throws Exception {
        BigDecimal realValue = BigDecimal.valueOf(amount * Math.pow(10.0, 18));
//        Function function = new Function(
//                "transfer",
//                Arrays.asList(
//                        new Address(to),
//                        new Uint256(amountInWei.toBigInteger())), new Uint256(realValue.toBigInteger())),
//        Collections.emptyList());
//
//        BigDecimal realValue = amount.multiply(decimal);
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to),
                        new Uint256(realValue.toBigInteger())), Collections.emptyList());


        String data = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                contractAddress,
                data);
        return signData(rawTransaction, rememberEth);
    }


}
