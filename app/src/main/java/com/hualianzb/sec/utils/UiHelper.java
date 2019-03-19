package com.hualianzb.sec.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.models.SecTransactionBean;
import com.hualianzb.sec.ui.activitys.AddAddressActivity;
import com.hualianzb.sec.ui.activitys.AddressBookActivity;
import com.hualianzb.sec.ui.activitys.BackupMnemonicsOneActy;
import com.hualianzb.sec.ui.activitys.BackupMnemonicsThreeActy;
import com.hualianzb.sec.ui.activitys.BackupMnemonicsTwoActy;
import com.hualianzb.sec.ui.activitys.ChangeAddressActivity;
import com.hualianzb.sec.ui.activitys.ChangePassActy;
import com.hualianzb.sec.ui.activitys.ChangeWalletActivity;
import com.hualianzb.sec.ui.activitys.CheckPassActivity;
import com.hualianzb.sec.ui.activitys.CreateInsertWalletActivity;
import com.hualianzb.sec.ui.activitys.CreateWalletActivity;
import com.hualianzb.sec.ui.activitys.HomePageActivity;
import com.hualianzb.sec.ui.activitys.ImportWalletActivity;
import com.hualianzb.sec.ui.activitys.IntroduceKeystoreAcy;
import com.hualianzb.sec.ui.activitys.IntroduceMnActy;
import com.hualianzb.sec.ui.activitys.IntroducePrivatekeyAcy;
import com.hualianzb.sec.ui.activitys.MakeCodeActivity;
import com.hualianzb.sec.ui.activitys.MakeMoneyActicity;
import com.hualianzb.sec.ui.activitys.ManagerWalletActy;
import com.hualianzb.sec.ui.activitys.TransactionRecordActy;
import com.hualianzb.sec.ui.activitys.TransactionRecordDetailAllActivity;
import com.hualianzb.sec.ui.activitys.TransactionRecordSecActivity;
import com.hualianzb.sec.ui.activitys.TransferActivity;
import com.hualianzb.sec.ui.activitys.WebActivity;


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

    public static void startBackupMnemonicsActy2(Context activity, String address) {
        Intent intent = new Intent(activity, BackupMnemonicsTwoActy.class);
        intent.putExtra("address", address);
        activity.startActivity(intent);
    }

    public static void startBackupMnemonicsActy3(Context activity, String myMnemonics) {
        Intent intent = new Intent(activity, BackupMnemonicsThreeActy.class);
        intent.putExtra("myMnemonics", myMnemonics);
        activity.startActivity(intent);
    }

    public static void startBackupMnemonicsActy1(Context activity, RememberSEC rememberEth) {
        Intent intent = new Intent(activity, BackupMnemonicsOneActy.class);
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
    public static void startTransferActivity(Activity activity, String title, String address, String tokenValue) {
        Intent intent = new Intent(activity, TransferActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("address", address);
        intent.putExtra("tokenValue", tokenValue);
        activity.startActivityForResult(intent, 100);
    }

    public static void TransactionRecordSecActivity(Context activity, String address, String kind, String money) {
        Intent intent = new Intent(activity, TransactionRecordSecActivity.class);
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

    public static void startChangeWalletRecordActyResult(Activity activity, String address, boolean isFromHome) {
        Intent intent = new Intent(activity, ChangeWalletActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("isFromHome", isFromHome);
        activity.startActivityForResult(intent, 100);
    }

    public static void startTransaAllActivity(Context activity, SecTransactionBean.ResultBean.ResultInChainBeanOrPool resultBean, String address) {
        Intent intent = new Intent(activity, TransactionRecordDetailAllActivity.class);
        intent.putExtra("resultBean", resultBean);
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

    public static void startCheckPassActivity(Activity activity, String pass, int requestCode) {
        Intent intent = new Intent(activity, CheckPassActivity.class);
        intent.putExtra("pass", pass);
        intent.putExtra("requestCode", requestCode);
        activity.startActivityForResult(intent, requestCode);
    }
}


