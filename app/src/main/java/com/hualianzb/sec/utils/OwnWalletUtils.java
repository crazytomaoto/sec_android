package com.hualianzb.sec.utils;

import android.util.Log;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;

import static io.github.novacrypto.bip32.networks.Bitcoin.MAIN_NET;

public class OwnWalletUtils extends WalletUtils {

    // OVERRIDING THOSE METHODS BECAUSE OF CUSTOM WALLET NAMING (CUTING ALL THE TIMESTAMPTS FOR INTERNAL STORAGE)

    public static String generateFullNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(password, destinationDirectory, true);
    }
//
//    public static String generateLightNewWalletFile(String password, File destinationDirectory)
//            throws NoSuchAlgorithmException, NoSuchProviderException,
//            InvalidAlgorithmParameterException, CipherException, IOException {
//        return generateNewWalletFile(password, destinationDirectory, false);
//    }

//    public static String generateNewWalletFile(
//            String password, File destinationDirectory, boolean useFullScrypt, MyECKeyPair ecKeyPair)
//            throws CipherException, IOException, InvalidAlgorithmParameterException,
//            NoSuchAlgorithmException, NoSuchProviderException {
//        return generateWalletFile(password, ecKeyPair, destinationDirectory, useFullScrypt);
//    }

//    public static MyECKeyPair geECKeyPair(String mnemonics, String passphrase) {
//        byte[] seed = new SeedCalculator()
//                .withWordsFromWordList(English.INSTANCE)
//                .calculateSeed(Collections.singleton(mnemonics), passphrase);
////        MyECKeyPair ecKeyPair = MyECKeyPair.create(seed);
//        MyECKeyPair ecKeyPair = MyECKeyPair.create(seed);
//
//        return ecKeyPair;
//    }


    public static WalletFile getKeyStore(String password, ECKeyPair ecKeyPair, boolean useFullScrypt) throws CipherException {
        WalletFile walletFile = null;
        if (useFullScrypt) {
            walletFile = Wallet.createStandard(password, ecKeyPair);
        } else {
            walletFile = Wallet.createLight(password, ecKeyPair);
        }
        return walletFile;
    }
//
//    public static String generateWalletFile(
//            String password, MyECKeyPair ecKeyPair, File destinationDirectory, boolean useFullScrypt)
//            throws CipherException, IOException {
////        String fileName = getWalletFileName(walletFile);
////        File destination = new File(destinationDirectory, fileName);
////        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
////        objectMapper.writeValue(destination, walletFile);
//        return null;
//    }

//    private static String getWalletFileName(WalletFile walletFile) {
//        return walletFile.getAddress();
//    }

    public static String generateMnemonics() {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, sb::append);
        return sb.toString();
    }

    /**
     * generate key pair to create eth wallet
     * 生成KeyPair , 用于创建钱包
     */
    public static ECKeyPair generateKeyPair(String mnemonics) {
        String TAG = "++++";
        // 1. we just need eth wallet for now
        AddressIndex addressIndex = BIP44
                .m()
                .purpose44()
                .coinType(60)
                .account(0)
                .external()
                .address(0);

        byte[] seed = new SeedCalculator().calculateSeed(mnemonics, "");
        ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(seed, MAIN_NET);
        Log.i(TAG, "mnemonics:" + mnemonics);
        String extendedBase58 = rootKey.extendedBase58();
        Log.i(TAG, "extendedBase58:" + extendedBase58);

        // 3. get child private key deriving from master/root key
        ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
        String childExtendedBase58 = childPrivateKey.extendedBase58();
        Log.i(TAG, "childExtendedBase58:" + childExtendedBase58);

        // 4. get key pair
        byte[] privateKeyBytes = childPrivateKey.getKey();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyBytes);
        return keyPair;
    }

    //通过私钥获取ECKeyPair-----导入钱包使用
    public static ECKeyPair getKeyPair(String privateKey) {
        ECKeyPair keyPair = null;
        try {
            keyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
//            keyPair = ECKeyPair.create(new BigInteger(privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPair;
    }

}