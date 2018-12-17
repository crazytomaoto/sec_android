package com.hualianzb.sec.ui;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.utils.MyWeb3jUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.utils.NumberUtils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.hualianzb.sec.utils.Util.getIndexNoneZore;

/**
 * Date:2018/10/23
 * auther:wangtianyun
 * describe:
 */
public class Myservice extends IntentService {

    public static final String TAG = "MyService";
    private RememberEth reme;
    private ArrayList<String> test1;
    private Map<String, ArrayList<String>> mapWalletTokens;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    };

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public Myservice(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        test1 = new ArrayList<>();
        mapWalletTokens = new HashMap<>();
    }

    //服务执行的操作
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mapWalletTokens = PlatformConfig.getMap(Constant.SpConstant.ALLKINDTOKEN);
        test1 = mapWalletTokens.get(reme.getAddress());
        if (null == test1 || test1.isEmpty() || test1.size() <= 0) {
            test1 = new ArrayList<String>();
            test1.add("ETH");
            test1.add("CEC");
        }
        for (int i = 0; i < test1.size(); i++) {
            String tokenAddress = test1.get(i);
            if (tokenAddress.equals("ETH")) {
                TaskCheckBanlance taskCheckBanlance = new TaskCheckBanlance(i);
                taskCheckBanlance.execute();
                continue;
            }
            if (tokenAddress.equals("CEC")) {
                AccountsTask task1 = new AccountsTask("CEC", i);
                task1.execute(RequestHost.url);
                continue;
            }
            if (tokenAddress.equals("SEC")) {
                AccountsTask task1 = new AccountsTask("SEC", i);
                task1.execute(RequestHost.url);
                continue;
            }
            if (tokenAddress.equals("INT")) {
                AccountsTask task1 = new AccountsTask("INT", i);
                task1.execute(RequestHost.url);
                continue;
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    private class AccountsTask extends AsyncTask<String, String, String> {
        private String tag;
        private int nowIndex;

        public AccountsTask(String tag, int nowIndex) {
            this.tag = tag;
            this.nowIndex = nowIndex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            // //获取余额
            Web3j web3j = MyWeb3jUtil.getWeb3jInstance();
            String xAddress = null;
            try {
                switch (tag) {
                    case "CEC":
                        xAddress = reme.getAddress().toString().trim().substring(2);
                        result = web3j.ethCall(Transaction.createEthCallTransaction(reme.getAddress(), RequestHost.cecToken, "0x70a08231000000000000000000000000" + xAddress), DefaultBlockParameterName.PENDING).send().getValue();
                        break;
                    case "SEC":
                        xAddress = reme.getAddress().toString().trim().substring(2);
                        result = web3j.ethCall(Transaction.createEthCallTransaction(reme.getAddress(), RequestHost.secToken, "0x70a08231000000000000000000000000" + xAddress), DefaultBlockParameterName.PENDING).send().getValue();

                        break;
                    case "INT":
                        xAddress = reme.getAddress().toString().trim().substring(2);
                        result = web3j.ethCall(Transaction.createEthCallTransaction(reme.getAddress(), RequestHost.intToken, "0x70a08231000000000000000000000000" + xAddress), DefaultBlockParameterName.PENDING).send().getValue();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!StringUtils.isEmpty(result)) {
                return result.substring(2);
            } else {
                return "000";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //得到16进制去除0x0000……的16进制字符串
            int index = getIndexNoneZore(result);
            if (index > 0) {
                String noZeroResult = result.substring(index);
                String str = new BigInteger(noZeroResult, 16).toString(10);
                String money = (Double.parseDouble(str) / (Math.pow(10, 18))) + "";
//                mmp.put(tag, money);
                switch (tag) {
                    case "CEC":
                        if (nowIndex == test1.size() - 1) {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.CECTOKEN_LAST;
                            message.obj = money;
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.CECTOKEN;
                            message.obj = money;
                            handler.sendMessage(message);
                        }
                        break;
                    case "SEC":
                        if (nowIndex == test1.size() - 1) {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.SECTOKEN_LAST;
                            message.obj = money;
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.SECTOKEN;
                            message.obj = money;
                            handler.sendMessage(message);
                        }
                        break;
                    case "INT":
                        if (nowIndex == test1.size() - 1) {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.INTTOKEN_LAST;
                            message.obj = money;
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.INTTOKEN;
                            message.obj = money;
                            handler.sendMessage(message);
                        }
                        break;
                }
            } else {
                switch (tag) {
                    case "CEC":
                        if (nowIndex == test1.size() - 1) {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.CECTOKEN_LAST;
                            message.obj = "0";
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = GlobalMessageType.MainRequest.CECTOKEN_LAST;
                            message.obj = "0";
                            handler.sendMessage(message);
                        }
                        break;
                    case "ETH":
                        Message message = new Message();
                        message.what = GlobalMessageType.MainRequest.ETHTOKEN;
                        message.obj = "0";
                        handler.sendMessage(message);
                        break;
                    case "SEC":
                        if (nowIndex == test1.size() - 1) {
                            Message messagess = new Message();
                            messagess.what = GlobalMessageType.MainRequest.SECTOKEN_LAST;
                            messagess.obj = "0";
                            handler.sendMessage(messagess);
                        } else {
                            Message messagess = new Message();
                            messagess.what = GlobalMessageType.MainRequest.SECTOKEN;
                            messagess.obj = "0";
                            handler.sendMessage(messagess);
                        }
                        break;
                    case "INT":
                        if (nowIndex == test1.size() - 1) {
                            Message messagess = new Message();
                            messagess.what = GlobalMessageType.MainRequest.INTTOKEN_LAST;
                            messagess.obj = "0";
                            handler.sendMessage(messagess);
                        } else {
                            Message messagess = new Message();
                            messagess.what = GlobalMessageType.MainRequest.INTTOKEN;
                            messagess.obj = "0";
                            handler.sendMessage(messagess);
                        }
                        break;
                }
            }
        }

    }

    //当前钱包的余额
    private class TaskCheckBanlance extends AsyncTask<String, String, String> {
        private int nowIndex;

        public TaskCheckBanlance(int nowIndex) {
            this.nowIndex = nowIndex;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            EthGetBalance ethGetBalance1 = null;
            try {
                ethGetBalance1 = MyWeb3jUtil.getWeb3jInstance().ethGetBalance(reme.getAddress(), DefaultBlockParameterName.LATEST).send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BigDecimal nig = Convert.fromWei(ethGetBalance1.getBalance().toString(), Convert.Unit.ETHER);
            result = nig.stripTrailingZeros().toPlainString();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            double myBalance = NumberUtils.round(Double.parseDouble(s), 2);

            if (nowIndex == test1.size() - 1) {
                Message message = new Message();
                message.obj = myBalance + "";
                message.what = GlobalMessageType.MainRequest.ETHTOKEN_LAST;
                handler.sendMessage(message);
            } else {
                Message message = new Message();
                message.obj = myBalance + "";
                message.what = GlobalMessageType.MainRequest.ETHTOKEN;
                message.obj = handler.sendMessage(message);
                handler.sendMessage(message);
            }
            PlatformConfig.setValue(Constant.SpConstant.MYBANLANCE, myBalance);
        }

    }

    //销毁服务时调用
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

}
