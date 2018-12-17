package com.hualianzb.sec.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hualianzb.sec.models.AllTransBean;
import com.hualianzb.sec.models.EthTransLogBean;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.activitys.AddAddressActivity;
import com.hualianzb.sec.ui.activitys.AddPropertyActivity;
import com.hualianzb.sec.ui.activitys.AddressBookActivity;
import com.hualianzb.sec.ui.activitys.BackupMnemonicsActy1;
import com.hualianzb.sec.ui.activitys.BackupMnemonicsActy2;
import com.hualianzb.sec.ui.activitys.BackupMnemonicsActy3;
import com.hualianzb.sec.ui.activitys.ChangeAddressActivity;
import com.hualianzb.sec.ui.activitys.ChangePassActy;
import com.hualianzb.sec.ui.activitys.ChangeWalletRecordActy;
import com.hualianzb.sec.ui.activitys.CreateInsertWalletActivity;
import com.hualianzb.sec.ui.activitys.CreateWalletActivity;
import com.hualianzb.sec.ui.activitys.GasActivity;
import com.hualianzb.sec.ui.activitys.HomePageActivity;
import com.hualianzb.sec.ui.activitys.ImportWalletActivity;
import com.hualianzb.sec.ui.activitys.IntroduceKeystoreAcy;
import com.hualianzb.sec.ui.activitys.IntroduceMnActy;
import com.hualianzb.sec.ui.activitys.IntroducePrivatekeyAcy;
import com.hualianzb.sec.ui.activitys.MakeCodeActivity;
import com.hualianzb.sec.ui.activitys.MakeMoneyActicity;
import com.hualianzb.sec.ui.activitys.ManagerWalletActy;
import com.hualianzb.sec.ui.activitys.OutKeyStoreAcy;
import com.hualianzb.sec.ui.activitys.ServiceAgreementAct;
import com.hualianzb.sec.ui.activitys.TransactionRecordActivity;
import com.hualianzb.sec.ui.activitys.TransactionRecordActy;
import com.hualianzb.sec.ui.activitys.TransactionRecordDetailAllActivity;
import com.hualianzb.sec.ui.activitys.TransactionRecordDetailEthActivity;
import com.hualianzb.sec.ui.activitys.TransactionRecordEthActivity;
import com.hualianzb.sec.ui.activitys.TransactionRecordSecActivity;
import com.hualianzb.sec.ui.activitys.TransferActivity;
import com.hualianzb.sec.ui.activitys.WebActivity;

import java.util.ArrayList;


/**
 * Created by wty on 2018/4/3.
 */

public class UiHelper {
    public static void startWebActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void startActyCreateInsertWallet(Context activity) {
        Intent intent = new Intent(activity, CreateInsertWalletActivity.class);
        activity.startActivity(intent);
    }

    public static void startActyCreateWalletActivity(Context activity) {
        Intent intent = new Intent(activity, CreateWalletActivity.class);
        activity.startActivity(intent);
    }

    public static void startActyImportWalletActivity(Context activity) {
        Intent intent = new Intent(activity, ImportWalletActivity.class);
        activity.startActivity(intent);
    }

    public static void startBackupMnemonicsActy2(Context activity, String myPass, String address) {
        Intent intent = new Intent(activity, BackupMnemonicsActy2.class);
        intent.putExtra("myPass", myPass);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void startBackupMnemonicsActy3(Context activity, String myMnemonics) {
        Intent intent = new Intent(activity, BackupMnemonicsActy3.class);
        intent.putExtra("myMnemonics", myMnemonics);
        activity.startActivity(intent);
    }

    public static void startBackupMnemonicsActy1(Context activity, RememberEth rememberEth) {
        Intent intent = new Intent(activity, BackupMnemonicsActy1.class);
        intent.putExtra("rememberEth", rememberEth);
        activity.startActivity(intent);
    }

    public static void startHomaPageAc(Context activity, String address) {
        Intent intent = new Intent(activity, HomePageActivity.class);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void goBackHomaPageAc(Context activity, String address, boolean isBackMy) {
        Intent intent = new Intent(activity, HomePageActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("isBackMy", isBackMy);
        activity.startActivity(intent);
    }

    public static void startServiceAgreementAct(Activity activity, boolean isFromGuide) {
        Intent intent = new Intent(activity, ServiceAgreementAct.class);
        intent.putExtra("isFromGuide", isFromGuide);
        activity.startActivity(intent);
    }

    public static void startServiceAgreementAct(Activity activity, boolean isFromGuide, int requestCode) {
        Intent intent = new Intent(activity, ServiceAgreementAct.class);
        intent.putExtra("isFromGuide", isFromGuide);
        intent.putExtra("from", requestCode);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startManagerWalletActy(Context activity) {
        Intent intent = new Intent(activity, ManagerWalletActy.class);
        activity.startActivity(intent);
    }

    public static void startMakeMoneyActicity(Context activity, String address, String money, boolean isFromHomePage) {
        Intent intent = new Intent(activity, MakeMoneyActicity.class);
        intent.putExtra("address", address);
        intent.putExtra("isFromHomePage", isFromHomePage);
        intent.putExtra("money", money);
        activity.startActivity(intent);
    }

    public static void startOutKeyStoreAcy(Context activity, String address) {
        Intent intent = new Intent(activity, OutKeyStoreAcy.class);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void startChangePassActy(Context activity, String address) {
        Intent intent = new Intent(activity, ChangePassActy.class);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void startTransactionRecordActy(Context activity, String address) {
        Intent intent = new Intent(activity, TransactionRecordActy.class);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    /**
     * @param activity
     * @param title
     * @param address
     * @param tokenValue 当前钱包所拥有的token数量
     */
    public static void startTransferActivity(Context activity, String title, String address, String tokenValue) {
        Intent intent = new Intent(activity, TransferActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("address", address);
        intent.putExtra("tokenValue", tokenValue);
        activity.startActivity(intent);
    }

    public static void startTransactionRecordActivity(Context activity, String address, String kind, String money) {
        Intent intent = new Intent(activity, TransactionRecordActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("kind", kind);
        intent.putExtra("money", money);
        activity.startActivity(intent);
    }

    public static void TransactionRecordSecActivity(Context activity, String address, String kind, String money) {
        Intent intent = new Intent(activity, TransactionRecordSecActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("kind", kind);
        intent.putExtra("money", money);
        activity.startActivity(intent);
    }

    public static void startTransactionRecordEth(Context activity, String address, String kind, String money) {
        Intent intent = new Intent(activity, TransactionRecordEthActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("kind", kind);
        intent.putExtra("money", money);
        activity.startActivity(intent);
    }

    public static void startMakeCodeActivity(Context activity, String address) {
        Intent intent = new Intent(activity, MakeCodeActivity.class);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void startGasActivity(Context activity) {
        Intent intent = new Intent(activity, GasActivity.class);
        activity.startActivity(intent);
    }

    public static void startChangeWalletRecordActy(Context activity, String address, boolean isFromHome) {
        Intent intent = new Intent(activity, ChangeWalletRecordActy.class);
        intent.putExtra("address", address);
        intent.putExtra("isFromHome", isFromHome);
        activity.startActivity(intent);
    }

    public static void startChangeWalletRecordActyResult(Activity activity, String address, boolean isFromHome) {
        Intent intent = new Intent(activity, ChangeWalletRecordActy.class);
        intent.putExtra("address", address);
        intent.putExtra("isFromHome", isFromHome);
        activity.startActivityForResult(intent, 100);
    }

    public static void startTransaAllActivity(Context activity, AllTransBean.ResultBean resultBean, String address) {
        Intent intent = new Intent(activity, TransactionRecordDetailAllActivity.class);
        intent.putExtra("resultBean", resultBean);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }


    public static void startTraDetailEth02Activity(Context activity, EthTransLogBean.ResultBean resultBean, String time, String hashTrans, String address) {
        Intent intent = new Intent(activity, TransactionRecordDetailEthActivity.class);
        intent.putExtra("resultBean", resultBean);
        intent.putExtra("time", time);
        intent.putExtra("hashTrans", hashTrans);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void startAddPropertyActivity(Context activity, ArrayList<String> tokenKinds, String address) {
        Intent intent = new Intent(activity, AddPropertyActivity.class);
        intent.putExtra("tokenKinds", tokenKinds);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void startIntroduceMnActy(Context activity) {
        Intent intent = new Intent(activity, IntroduceMnActy.class);
        activity.startActivity(intent);
    }

    public static void startIntroduceKeystoreActy(Context activity) {
        Intent intent = new Intent(activity, IntroduceKeystoreAcy.class);
        activity.startActivity(intent);
    }

    public static void startIntroducePKActy(Context activity) {
        Intent intent = new Intent(activity, IntroducePrivatekeyAcy.class);
        activity.startActivity(intent);
    }

    public static void startAddressBookActy(Activity activity, boolean isFromMy) {//isFromMy 是不是从“我的页面”进入，如果是，不显示扫面的按钮
        Intent intent = new Intent(activity, AddressBookActivity.class);
        intent.putExtra("isFromMy", isFromMy);
        activity.startActivityForResult(intent, 101);
    }

    public static void startChangeAddressBookActys(Context activity, int bookIndex) {
        Intent intent = new Intent(activity, ChangeAddressActivity.class);
        intent.putExtra("bookIndex", bookIndex);
        activity.startActivity(intent);
    }

    public static void startAddAddressBookActy(Context activity) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        activity.startActivity(intent);
    }
}


