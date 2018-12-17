package com.hualianzb.sec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//
//    public String signContractTransaction(String contractAddress, String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigDecimal amount, BigDecimal decimal, HLWallet wallet, String password) throws IOException, CipherException {
//        BigDecimal realValue = amount.multiply(decimal);
//        Function function = new Function("transfer", Arrays.asList(new Address(to), new Uint256(realValue.toBigInteger())), Collections.emptyList());
//        String data = FunctionEncoder.encode(function);
//        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
//        return signData(rawTransaction, wallet, password);
//    }
//
//    public String signedEthTransactionData(String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigDecimal amount, HLWallet wallet, String password) throws Exception { // 把十进制的转换成ETH的Wei, 1ETH = 10^18Wei
//        BigDecimal amountInWei = Convert.toWei(amount.toString(), Convert.Unit.ETHER);
//        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, amountInWei.toBigInteger());
//        return signData(rawTransaction, wallet, password);
//    }

}
