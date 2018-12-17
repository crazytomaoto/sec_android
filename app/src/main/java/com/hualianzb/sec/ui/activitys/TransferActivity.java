package com.hualianzb.sec.ui.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.activity.CaptureActivity;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.models.RequestHasParams;
import com.hualianzb.sec.models.SECSendRawBean;
import com.hualianzb.sec.models.SECTransResponseBean;
import com.hualianzb.sec.models.TagBean;
import com.hualianzb.sec.models.TransBean;
import com.hualianzb.sec.models.TransResultBean;
import com.hualianzb.sec.models.TransactionByHashBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.JsonUtil;
import com.hualianzb.sec.utils.MyWeb3jUtil;
import com.hualianzb.sec.utils.OwnWalletUtils;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthSign;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hualianzb.sec.commons.constants.Constant.SpConstant.SWEEP;

/**
 * Date:2018/8/16
 * auther:wangtianyun
 * describe:转账
 */
public class TransferActivity extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    Button btnNext;
    @BindView(R.id.ed_toadress)
    EditText edToadress;
    @BindView(R.id.tv_gas)
    TextView tv_gas;
    @BindView(R.id.ed_senMoney)
    EditText edSenMoney;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.ed_remark)
    EditText edRemark;
    private String myGetAdress;
    private boolean isPassRight = false;
    private Dialog dialog;
    private String address;
    private Map<String, RememberEth> map;
    private RememberEth rememberEth;
    private double setMoney;
    private String title;
    double now;
    int nowProgress = 0;
    private long gasPriceMin = 25200;
    private BigInteger gas = BigInteger.valueOf(25200);
    //    private long gasPriceMax = 1260000;
    private int per = 12600;
    private BigInteger myLimit = BigInteger.valueOf(60000);
    private KProgressHUD hud2;
    private StateBarUtil stateBarUtil;
    private RawTransaction rawTransaction;
    private String signedTransactionData;
    private Timer timer = new Timer();
    private TransBean transBean = new TransBean();
    private static final int MY_PERMISSION_REQUEST_CODE = 0x124;
    private ECKeyPair ecKeyPair = null;
    private Credentials credentials = null;
    private String tokenValue;
    private double myBalance;
    private String token;
    private Map<String, String> tranRemark;
    //任务
    TimerTask task1 = new TimerTask() {
        public void run() {
            Message msg4 = new Message();
            msg4.what = 444;
            handler.sendMessage(msg4);
        }
    };
    TimerTask task2 = new TimerTask() {
        public void run() {
            Message msg5 = new Message();
            if (null != ecKeyPair) {
                Log.e("web3", "ekeypair" + ecKeyPair.getPrivateKey().toString(16));
                msg5.what = 555;
                handler.sendMessage(msg5);
                task2.cancel();
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 444:
                    if (null != credentials) {
                        task1.cancel();
                        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                        signedTransactionData = Numeric.toHexString(signMessage);
                        Message msg2 = new Message();
                        msg2.what = 222;
                        handler.sendMessage(msg2);
                    }
                    break;
                case 555:
                    credentials = Credentials.create(ecKeyPair);
                    timer.schedule(task1, 0, 1000);
                    break;
                case 333:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            ecKeyPair = OwnWalletUtils.getKeyPair(rememberEth.getPrivateKey());
                        }
                    }.start();
                    timer.schedule(task2, 0, 1000);
                    break;
                case 222:
                    if (!StringUtils.isEmpty(signedTransactionData)) {

                        test(signedTransactionData);
//
//                        dolastStemp(signedTransactionData);
//
//
//                        Web3j web3j = Web3jFactory.build(new HttpService(RequestHost.secTestUrl));
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                web3j.ethSendRawTransaction(signedTransactionData);
////                                hud2.dismiss();
//                            }
//                        });
//                            dolastSECStemp(signedTransactionData);
                        //                        dolastStemp(signedTransactionData);
                    }
                    break;
            }
        }
    };


    /**
     * 发起交易
     *
     * @param signedTransactionData 签名的交易哈希
     */
    private void dolastStemp(String signedTransactionData) {
        List<String> list = new ArrayList<>();
        transBean.setId(1);
        if (title.equals("SEC")) {
            transBean.setMethod("sec_sendRawTransaction");
        }
        if (title.equals("ETH")) {
            transBean.setMethod("eth_sendRawTransaction");
        }
        transBean.setJsonrpc("2.0");
        transBean.setParams(list);
        list.add(signedTransactionData);
        String json = JSON.toJSONString(transBean);
        Log.e("web3", "json++\n" + json);
        new Thread(new Runnable() {
            @Override

            public void run() {
                RequestParams params = null;
                if (title.equals("SEC")) {
                    params = new RequestParams(RequestHost.secTestUrl);

                }
                if (title.equals("ETH")) {
                    params = new RequestParams(RequestHost.url);
                }
                params.setAsJsonContent(true);
                params.setBodyContent(json);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        hud2.dismiss();
                        TransResultBean transResultBean = JSON.parseObject(result, TransResultBean.class);
                        if (null != transResultBean) {
                            if (StringUtils.isEmpty(transResultBean.getResult())) {
                                ToastUtil.show(TransferActivity.this, "转账失败");
                            } else {
                                if (transResultBean.getResult().length() > 64) {
                                    ToastUtil.show(TransferActivity.this, "转账完成");
                                    String myRemark = edRemark.getText().toString().trim();
                                    if (!StringUtils.isEmpty(myRemark)) {
                                        if (null == tranRemark) {
                                            tranRemark = new HashMap<>();
                                        }
                                        tranRemark.put(transResultBean.getResult(), myRemark);
                                        PlatformConfig.putMap(Constant.SpConstant.TRANSREMARKS, tranRemark);
                                    }

                                } else {
                                    ToastUtil.show(TransferActivity.this, "转账失败");
                                }
                            }
                        }
                        Log.e("web3", "result" + result);
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hud2.dismiss();
                        ToastUtil.show(TransferActivity.this, "转账失败");
                        Log.e("web3", ex.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        hud2.dismiss();
                        ToastUtil.show(TransferActivity.this, "转账取消");
                        dialog.dismiss();
                    }

                    @Override
                    public void onFinished() {
                        hud2.dismiss();
                        Log.e("web3", "onFinished");
                    }
                });

            }
        }
        ) {
        }.start();
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            title = bundle.getString("title");
            address = bundle.getString("address");
            tokenValue = bundle.getString("tokenValue");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            title = savedInstanceState.getString("title");
            address = savedInstanceState.getString("address");
            tokenValue = savedInstanceState.getString("tokenValue");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();
    }

    private void initView() {
        hud2 = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(" 正在转账 ")
                .setCancellable(false);
        tvTitle.setText(title + "转账");
        nowProgress = 50;
        now = Math.pow(10, -9) * per * nowProgress;
        ivBackTop.setImageResource(R.drawable.icon_close_black);
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        tranRemark = new HashMap<>();
        tranRemark = PlatformConfig.getMap(Constant.SpConstant.TRANSREMARKS);
//        if ("SEC".equals(title)) {
        address = "0x" + address;
//        }

        if (map != null && !map.isEmpty() && map.values().size() > 0) {
            for (RememberEth remember : map.values()) {
                if (address.equals(remember.getAddress())) {
                    rememberEth = remember;
                    break;
                }
            }
        }
        tv_gas.setText(save8num(now + "") + " eth");
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 2) {
                    nowProgress = 2;
//                    now = per / Math.pow(10, 9);
                } else {
                    nowProgress = progress;
                }
                now = per * nowProgress / Math.pow(10, 9);
                tv_gas.setText(save8num(now + "") + " eth");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        switch (title) {
            case "CEC":
                token = RequestHost.cecToken;
                break;
            case "ETH":
                token = RequestHost.ethToken;
                break;
            case "SEC":
                token = RequestHost.ethToken;
                break;
            case "INT":
                token = RequestHost.ethToken;
                break;
        }
    }

    @OnClick({R.id.btn_next, R.id.iv_gas, R.id.iv_contanct})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_gas:
                UiHelper.startGasActivity(this);
                break;
            case R.id.btn_next:
                if (!isNetworkAvailable(this)) {
                    ToastUtil.show(this, getString(R.string.net_work_err));
                    return;
                }
                goNext();
                break;
            case R.id.iv_contanct:
                UiHelper.startAddressBookActy(this, false);
                break;
        }
    }

    //继续进行
    private void goNext() {
        myGetAdress = edToadress.getText().toString().trim();
        String strSendMoney = edSenMoney.getText().toString().trim();//输入的钱数
        if (StringUtils.isEmpty(myGetAdress)) {
            showCheckDialog(this, "钱包地址为空");
            return;
        }
        if (myGetAdress.length() != 42 || !myGetAdress.substring(0, 2).equals("0x")) {
            showCheckDialog(this, "钱包地址不符合规则");
            return;
        }
        if (myGetAdress.equals(address)) {
            showCheckDialog(this, "同一地址之间不能转账哦");
            return;
        }
        if (StringUtils.isEmpty(strSendMoney) || Double.parseDouble(strSendMoney) <= 0) {
            showCheckDialog(this, "转账金额为空");
            return;
        }

        if (title.equals("ETH") || title.equals("eth")) {
            if (Double.parseDouble(tokenValue) <= 0) {
                showCheckDialog(this, "没有足够的ETH");
                return;
            }
            if (Double.parseDouble(tokenValue) <= Double.parseDouble(strSendMoney)) {
                showCheckDialog(this, "没有足够的ETH");
                return;
            }
            String fee = save8num(now + "");
            double feeDouble = Double.parseDouble(fee);
            if (Double.parseDouble(tokenValue) < (Double.parseDouble(strSendMoney) + feeDouble)) {
                showCheckDialog(this, "没有足够的ETH");
                return;
            }
        } else if (title.equals("SEC") || title.equals("sec")) {
            if (Double.parseDouble(tokenValue) <= 0) {
                showCheckDialog(this, "没有足够的SEC");
                return;
            }
            if (Double.parseDouble(tokenValue) <= Double.parseDouble(strSendMoney)) {
                showCheckDialog(this, "没有足够的SEC");
                return;
            }
            String fee = save8num(now + "");
            double feeDouble = Double.parseDouble(fee);
            if (Double.parseDouble(tokenValue) < (Double.parseDouble(strSendMoney) + feeDouble)) {
                showCheckDialog(this, "没有足够的SEC");
                return;
            }
        } else {
            if (Double.parseDouble(tokenValue) <= 0) {
                showCheckDialog(this, "余额不足");
                return;
            }
            if (Double.parseDouble(tokenValue) < Double.parseDouble(strSendMoney)) {
                showCheckDialog(this, "余额不足");
                return;
            }
            myBalance = PlatformConfig.getDouble(Constant.SpConstant.MYBANLANCE);
            if (myBalance <= 0) {
                showCheckDialog(this, "没有足够的ETH");
                return;
            }
        }
        showTradeDetailDialog(TransferActivity.this);
    }


    //转账校验
    private void showCheckDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.layout_toast);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView yes = dialog.findViewById(R.id.tv__ok);
        TextView tv_content = dialog.findViewById(R.id.tv_content);
        tv_content.setText(content);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //密码校验
    private void showPasssDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_bankup_mnemonics);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView yes = dialog.findViewById(R.id.tv__ok);
        final EditText ed_pass = dialog.findViewById(R.id.ed_pass);
        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myPass = ed_pass.getText().toString().trim();
                if (StringUtils.isEmpty(myPass)) {
                    ToastUtil.show(TransferActivity.this, "密码为空");
//                    isPassRight = false;
                } else {
                    if (!myPass.equals(rememberEth.getPass())) {
                        ToastUtil.show(TransferActivity.this, "密码不正确");
//                        isPassRight = false;
                    } else {
                        //转账
                        try {
//                            switch (title) {
//                                case "ETH":
//                            tranETH();
//                                    break;
//                                case "SEC":
//                            tranSEC();
//                                    break;
//
//                            }
//                            if (title.equals("ETH") || title.equals("eth")) {
//                                tranETH();
//                            } else {
                            tranToken();//ETH币种转token
//                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        isPassRight = true;
                        dialog.dismiss();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 101:
                if (resultCode == 1) {
                    if (null != data) {
                        String result = data.getExtras().getString("result");
                        if (!StringUtils.isEmpty(result)) {
                            String[] resultArray = result.split("###");
                            if (resultArray != null && resultArray.length > 0) {
                                myGetAdress = resultArray[0];
                                edToadress.setText(myGetAdress);
                                String money = resultArray[1];
                                if (!StringUtils.isEmpty(money)) {
                                    setMoney = Double.parseDouble(resultArray[1]);
                                    edSenMoney.setText(setMoney + "");
                                } else {
                                    setMoney = 0.00;
                                    edSenMoney.setText(setMoney + "");
                                }
                            }
                        }
                    }
                }
                if (resultCode == 2) {
                    if (null != data) {
                        myGetAdress = data.getExtras().getString("address");
                        if (!StringUtils.isEmpty(address)) {
                            edToadress.setText(myGetAdress);
                            myGetAdress = 0.00 + "";
                            edSenMoney.setText(myGetAdress);
                        }
                    }
                }
                break;
            default:
                break;
        }


        //二维码扫描结果
        if (requestCode == SWEEP) {

            String result = "";
            try {
                result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            } catch (Exception e) {
                ToastUtil.show(this, R.string.scan_error);
            }
            if (!StringUtils.isEmpty(result)) {


            } else {
                ToastUtil.show(this, "扫描失败");
            }

        }
    }

    public void showTradeDetailDialog(final Activity context) {
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(R.layout.dialog_trade_detail);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.show();
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        TextView tv_to = dialog.findViewById(R.id.tv_to);
        TextView tv_from = dialog.findViewById(R.id.tv_from);
        TextView tv_gas = dialog.findViewById(R.id.tv_gas);
        TextView tv_money = dialog.findViewById(R.id.tv_money);
        TextView tv_sure = dialog.findViewById(R.id.tv_sure);
        TextView tv_money_kind = dialog.findViewById(R.id.tv_money_kind);
        tv_to.setText(myGetAdress);
        tv_from.setText(rememberEth.getAddress());
        tv_money.setText(edSenMoney.getText().toString().trim());
        tv_gas.setText(save8num(now + ""));
        tv_money_kind.setText(title);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasssDialog(TransferActivity.this);
                dialog.dismiss();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void tranToken() throws Exception {
        hud2.show();
        EthGetTransactionCount ethGetTransactionCount = MyWeb3jUtil.getWeb3jInstance().ethGetTransactionCount(
                rememberEth.getAddress()
                , DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        double amount = Double.parseDouble(edSenMoney.getText().toString().trim());


        String mmp = NumberFormat.getInstance().format(now * Math.pow(10, 9) * 20);
        mmp = mmp.replaceAll(",", "");
        BigInteger gasPrice = BigInteger.valueOf(Long.parseLong(mmp));

//        BigInteger gasPrice = BigInteger.valueOf(Long.parseLong(now * Math.pow(10, 9) + ""));
        signContractTransaction(
                token,
                edToadress.getText().toString().trim(),
                nonce,
                gasPrice,
                myLimit,
                amount
        );//单位10的18次方
    }

    private void tranSEC() throws Exception {
        hud2.show();
        List<SECSendRawBean.ParamsBean> list = new ArrayList<>();
        SECSendRawBean.ParamsBean paramsBean = new SECSendRawBean.ParamsBean();
        paramsBean.setFrom(address);
        paramsBean.setTo(myGetAdress);
        paramsBean.setGas("0");
        paramsBean.setGasPrice("0");
        paramsBean.setData("0");
        paramsBean.setValue("0.01");

        list.add(paramsBean);

        SECSendRawBean bean = new SECSendRawBean();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("sec_sendRawTransaction");
        bean.setParams(list);

        String json = JSON.toJSONString(bean);
        RequestParams params = new RequestParams(RequestHost.secTestUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(json);


        Web3j web3j = Web3jFactory.build(new HttpService(RequestHost.secTestUrl));
        TransactionManager manager = new TransactionManager(web3j, address) {
            @Override
            public EthSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {
                return null;
            }
        };
        BigDecimal realValue = BigDecimal.valueOf(Double.parseDouble(edSenMoney.getText().toString()) * Math.pow(10.0, 18));
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(edToadress.getText().toString()),
                        new Uint256(realValue.toBigInteger())), Collections.emptyList());


        String data = FunctionEncoder.encode(function);
        EthSendTransaction sendTransaction = manager.sendTransaction(gas, myLimit, edToadress.getText().toString(), signedTransactionData, BigInteger.valueOf(Long.parseLong("0")));
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                x.http().post(params, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.e("web3", "sec_sendRawTransaction-result+\n" + result);
//                        if (!StringUtils.isEmpty(result)) {
//                            SECTransResponseBean transResponseBean = JsonUtil.parseJson(result, SECTransResponseBean.class);
//                            if (null != transResponseBean) {
//                                SECTransResponseBean.ResultBean resultBean = transResponseBean.getResult();
//                                if (null != resultBean) {
//                                    String statrus = resultBean.getStatus();//  1 成功 0 不成功，答应info
//                                    String info = resultBean.getInfo();
//                                    String txHash = resultBean.getTxHash();
//                                    if (!StringUtils.isEmpty(statrus) && statrus.equals("1")) {
//                                        if (!StringUtils.isEmpty(txHash) && txHash.contains("0x") && txHash.length() >= 64) {
//                                            ToastUtil.show(TransferActivity.this, "转账完成");
//                                            String myRemark = edRemark.getText().toString().trim();
//                                            if (!StringUtils.isEmpty(myRemark)) {
//                                                if (null == tranRemark) {
//                                                    tranRemark = new HashMap<>();
//                                                }
//                                                tranRemark.put(txHash, myRemark);
//                                                PlatformConfig.putMap(Constant.SpConstant.TRANSREMARKS, tranRemark);
//                                            }
//                                        } else {
//                                            ToastUtil.show(TransferActivity.this, "转账失败");
//                                        }
//                                    } else {
//                                        ToastUtil.show(TransferActivity.this, "转账失败");
//                                    }
//                                }
//                                hud2.dismiss();
//                                dialog.dismiss();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        Log.e("web3", ex.toString());
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//                    }
//
//                    @Override
//                    public void onFinished() {
//                        Log.e("web3", "onFinished");
//                    }
//                });
//            }
//        }).start();


        hud2.show();
        EthGetTransactionCount ethGetTransactionCount = MyWeb3jUtil.getWeb3jInstance().ethGetTransactionCount(
                rememberEth.getAddress()
                , DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        double amount = Double.parseDouble(edSenMoney.getText().toString().trim());
        BigDecimal money = new BigDecimal(amount);

        String mmp = NumberFormat.getInstance().format(now * Math.pow(10, 9) * 20);
        mmp = mmp.replaceAll(",", "");
        BigInteger gasPrice = BigInteger.valueOf(Long.parseLong(mmp));
        signedEthTransactionData(
                edToadress.getText().toString().trim(),
                nonce,
                gasPrice,
                myLimit,
                money);
    }

    private void tranETH() throws Exception {
        hud2.show();
        EthGetTransactionCount ethGetTransactionCount = MyWeb3jUtil.getWeb3jInstance().ethGetTransactionCount(
                rememberEth.getAddress()
                , DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        double amount = Double.parseDouble(edSenMoney.getText().toString().trim());
        BigDecimal money = new BigDecimal(amount);

        String mmp = NumberFormat.getInstance().format(now * Math.pow(10, 9) * 20);
        mmp = mmp.replaceAll(",", "");
        BigInteger gasPrice = BigInteger.valueOf(Long.parseLong(mmp));
        signedEthTransactionData(
                edToadress.getText().toString().trim(),
                nonce,
                gasPrice,
                myLimit,
                money);
    }

    //保留八位小数
    private String save8num(String num) {
        Double cny = Double.parseDouble(num);//转换成Double
        DecimalFormat df = new DecimalFormat("0.00000000");//格式化
        String now8 = df.format(cny);
        return now8;
    }

    /**
     * RawTransaction.createTransaction（）//这个方法是  非ETH交易所的token转账的方法
     *
     * @param contractAddress
     * @param to
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param amount
     * @throws Exception
     */
    public void signContractTransaction(String contractAddress, String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, double amount) throws Exception {
        BigDecimal realValue = BigDecimal.valueOf(amount * Math.pow(10.0, 18));
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to),
                        new Uint256(realValue.toBigInteger())), Collections.emptyList());


        String data = FunctionEncoder.encode(function);
        rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                contractAddress,
                data);
        Message msg3 = new Message();
        msg3.what = 333;
        handler.sendMessage(msg3);
    }


    /**
     * ETH 转账离线签名
     * <p>
     * RawTransaction.createEtherTransaction（）//这个方法是ETH转账的方法
     *
     * @param to       转入的钱包地址
     * @param nonce    以太坊nonce
     * @param gasPrice gasPrice
     * @param gasLimit gasLimit
     * @param amount   转账的eth数量
     */
    public void signedEthTransactionData(String to,
                                         BigInteger nonce,
                                         BigInteger gasPrice,
                                         BigInteger gasLimit,
                                         BigDecimal amount) {
        // 把十进制的转换成ETH的Wei, 1ETH = 10^18 Wei
        BigDecimal amountInWei = Convert.toWei(amount.toString(), Convert.Unit.ETHER);
        rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, amountInWei.toBigInteger());
        Message msg3 = new Message();
        msg3.what = 333;
        handler.sendMessage(msg3);
    }

    private void dolastSECStemp(String signedTransactionData, String address) throws IOException {
//        Web3j web3j = Web3jFactory.build(new HttpService(RequestHost.secTestUrl));
//        TransactionManager manager = new TransactionManager(web3j, address) {
//            @Override
//            public EthSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {
//                return null;
//            }
//        };
//        manager.sendTransaction()


        new Thread(new Runnable() {
            @Override
            public void run() {
                Message sss = new Message();
                sss.what = 23232;
                EthSendTransaction rr = null;
//                rr = web3j.ethSendRawTransaction(signedTransactionData).send();

                Log.e("web3", rr.getTransactionHash().toString());
            }
        }).run();
    }


    private void test(String signedTransactionData) {
//        Web3j web3j = Web3jFactory.build(new HttpService(RequestHost.secTestUrl));
////        Request<?, EthSign> request = web3j.ethSign(address, signedTransactionData);
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                EthSign ethSign = null;
//                try {
//                    //发送交易
//                    EthSendTransaction ethSendTransaction =
//                            null;
//                    ethSendTransaction = web3j.ethSendRawTransaction(signedTransactionData).sendAsync().get();
//                    String transactionHash = ethSendTransaction.getTransactionHash();
//                    for (int i = 0; ; i++) {
//                        if (!StringUtils.isEmpty(transactionHash)) {
//                            Log.e("web", "result-------" + transactionHash);
//                            hud2.dismiss();
//                            break;
//                        }
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
////                for (int i = 0; ; i++) {
////                    String result = ethSign.getSignature();
////                    if (!StringUtils.isEmpty(result)) {
////                        Log.e("web", "result-------" + result);
////                        hud2.dismiss();
////                        break;
////                    }
////                }
//            }
//        }).start();

    }

    private void getData() {
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());
        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        byte v = signatureData.getV();
    }
}
